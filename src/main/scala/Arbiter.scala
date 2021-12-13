import chisel3._
import chisel3.util._

// Only one will be ready, as we cannot take two values
// This would need a shadow register, a reasonable optimisation
// Without optimisation one channel can only take one data every 2 clock cycles

// the following should error (in the Module), as this is the wrong direction
// out.ready := a.ready

/*
class MyDecoupledIO[T <: Data](gen: T) extends Bundle {
  val ready = Input(Bool())
  val valid = Output(Bool())
  val bits = Output(gen)
}
*/

/*
object MyImplicits {
  implicit class TreeReducter[T: Manifest](s: Seq[T]) {
    def treeReduce[T: Manifest](op: (T, T) => T): T = {

      val l = s.length
      require(l > 0, "Cannot apply reduction on a Seq of size 0")
      l match {
        case 1 => s(0)
        case 2 => op(s(0), s(1))
        case _ => {
          val ns = new Array[T]((l+1)/2)
          for (i <- 0 until l/2) {
            ns(i) = op(s(i * 2), s(i * 2 + 1))
          }
          if (l % 2 == 1) {
            ns(l / 2) = s(l - 1)
          }
          (Seq) (ns).treeReduce(op)
        }
      }
    }
  }

}

 */

//- start fun_arbiter
class Arbiter[T <: Data: Manifest](n: Int, private val gen: T) extends Module {
  val io = IO(new Bundle {
    val in = Flipped(Vec(n, new DecoupledIO(gen)))
    val out = new DecoupledIO(gen)
  })

  // TODO: the odd case is not correct, try with 5
  def myTree[T: Manifest](s: Seq[T], op: (T, T) => T): T = {

    val l = s.length
    require(l > 0, "Cannot apply reduction on a Seq of size 0")
    l match {
      case 1 => s(0)
      case 2 => op(s(0), s(1))
      case _ => {
        val ns = new Array[T]((l+1)/2)
        for (i <- 0 until l/2) {
          ns(i) = op(s(i * 2), s(i * 2 + 1))
        }
        if (l % 2 == 1) {
          ns(l / 2) = s(l - 1)
        }
        myTree(ns, op)
      }
    }
  }

  def foo(a: DecoupledIO[T], b: DecoupledIO[T]) = {

    val regData = Reg(gen)
    val regValid = RegInit(false.B)
    val out = Wire(new DecoupledIO(gen))

    val turnA = RegInit(true.B)
    turnA := !turnA
    a.ready := !regValid & turnA
    b.ready := !regValid & !turnA

    when (regValid) {
      when (out.ready) {
        regValid := false.B
      }
    } .otherwise {
      when (turnA & a.valid) {
        regData := a.bits
        regValid := true.B
      } .elsewhen(!turnA & b.valid) {
        regData := b.bits
        regValid := true.B
      }
    }

    out.bits := regData
    out.valid := regValid
    out
  }

  def baz(a: DecoupledIO[T], b: DecoupledIO[T]) = {
    val out = Wire(new DecoupledIO(gen))
    out.bits := a.bits.asUInt() + b.bits.asUInt()
    a.ready := true.B
    b.ready := true.B
    out.valid := true.B
    out
  }
  // io.out <> io.in.reduceTree(foo)
  // io.out <> io.in.reduce(baz)
  io.out <> myTree(io.in, baz)
}
//- end

object Arbiter extends App {
  println((new chisel3.stage.ChiselStage).emitVerilog(new Arbiter(5, UInt(8.W))))
}
