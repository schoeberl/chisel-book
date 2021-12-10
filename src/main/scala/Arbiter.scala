import chisel3._
import chisel3.util._

// Only one will be ready, as we cannot take two values
// This would need a shadow register, a reasonable optimisation
// Without optimisation one channel can only take one data every 2 clock cycles

// the following should error (in the Module), as this is the wrong direction
// out.ready := a.ready

//- start fun_arbiter
class Arbiter[T <: Data](n: Int, private val gen: T) extends Module {
  val io = IO(new Bundle {
    val in = Flipped(Vec(n, new DecoupledIO(gen)))
    val out = new DecoupledIO(gen)
  })

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

  // io.out <> io.in.reduceTree(foo)
  io.out <> io.in.reduce(foo)
}
//- end

object Arbiter extends App {
  println((new chisel3.stage.ChiselStage).emitVerilog(new Arbiter(4, UInt(8.W))))
}
