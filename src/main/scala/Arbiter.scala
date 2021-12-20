import chisel3._
import chisel3.util._

import java.lang.Math.{floor, log10, pow}

// Only one will be ready, as we cannot take two values
// This would need a shadow register, a reasonable optimisation
// Without optimisation one channel can only take one data every 2 clock cycles

// the following should error (in the Module), as this is the wrong direction
// out.ready := a.ready


object TreeReduce {
  implicit class SeqToTreeReducible[T](xs: Seq[T]) {
    def treeReduce(op: (T,T) => T): T = {
      xs match {
        case Seq(x) => x
        case s => s.grouped(2).map {
          case Seq(x) => x
          case Seq(x,y) => op(x,y)
        }.toSeq.treeReduce(op)
      }
    }
  }
}
import TreeReduce.SeqToTreeReducible
// TODO Tjark's code did not need the Manifest
//- start fun_arbiter
class Arbiter[T <: Data: Manifest](n: Int, private val gen: T) extends Module {
  val io = IO(new Bundle {
    val in = Flipped(Vec(n, new DecoupledIO(gen)))
    val out = new DecoupledIO(gen)
  })

  // TODO: the odd case is not correct, try with 5
  // TODO: rewrite with Luca's idea
  def myUnfairTree[T: Manifest](s: Seq[T], op: (T, T) => T): T = {

    val l = s.length
    require(l > 0, "Cannot apply reduction on a Seq of size 0")
    l match {
      case 1 => s(0)
      case 2 => println("in case 2"); op(s(0), s(1))
      case _ => {
        // Maybe with Array[Any] we can avoid the Manifest thing
        val ns = new Array[T]((l+1)/2)
        for (i <- 0 until l/2) {
          ns(i) = op(s(i * 2), s(i * 2 + 1))
        }
        if (l % 2 == 1) {
          ns(l / 2) = s(l - 1)
        }
        myUnfairTree(ns, op)
      }
    }
  }

  def myTree[T: Manifest](s: Seq[T], op: (T, T) => T): T = {

    val n = s.length
    require(n > 0, "Cannot apply reduction on a Seq of size 0")

    println(s"Arbit between $n")

    n match {
      case 1 => s(0)
      case 2 => println("in case 2"); op(s(0), s(1))
      case _ =>
        val m =  pow(2, floor(log10(n-1)/log10(2))).toInt // number of nodes in upper level
        val ns = new Array[T](m)
        val k = 2 * (n - m)
        val p = n - k
        println(s"$m new nodes, $p promoted, and $k combined")
        // promote first few nodes directly up
        // this will only be done in the first round
        for (i <- 0 until p) {
          println(s"promote $i")
          ns(i) = s(i)
        }
        for (i <- 0 until k by 2) {
          println(s"combine ${p+i} and ${p+i+1} to ${p + i/2}")
          println(s"${s(p+i)}")
          println(s"${s(p+i+1)}")
          ns(p + i/2) = op(s(p+i), s(p+i+1))
        }
        myTree(ns, op)
    }
  }

  def myTreeFunctional[T: Manifest](s: Seq[T], op: (T, T) => T): T = {

    val n = s.length
    require(n > 0, "Cannot apply reduction on a Seq of size 0")

    n match {
      case 1 => s(0)
      case 2 => op(s(0), s(1))
      case _ =>
        val m =  pow(2, floor(log10(n-1)/log10(2))).toInt // number of nodes in upper level
        val k = 2 * (n - m)
        val p = n - k

        println(s"Start of $n")
        val l =  s.take(p)
        println(s"left: $l of ${l.length} elements")
        println(l)
        val r = s.drop(p).grouped(2).map {
          case Seq(a, b) => op(a, b)
        }.toSeq
        println(s"right: $r of ${r.length} elements")
        println(r)

        val ns = l ++ r
        println(s"all: $ns of ${ns.length} elements")

        myTreeFunctional(ns, op)
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

  def add(a: DecoupledIO[T], b: DecoupledIO[T]) = {
    val out = Wire(new DecoupledIO(gen))
    println(s"in add first $a")
    println(s"in add second $b")
    out.bits := a.bits.asUInt() + b.bits.asUInt()
    a.ready := true.B
    b.ready := true.B
    out.valid := true.B
    out
  }
  // io.out <> io.in.reduceTree(foo)
  // io.out <> io.in.reduce(foo)
  // io.out <> io.in.treeReduce(add)
  io.out <> myTreeFunctional(io.in, add)
}
//- end

object Arbiter extends App {

  println((new chisel3.stage.ChiselStage).emitVerilog(new Arbiter(7, UInt(8.W))))
}
