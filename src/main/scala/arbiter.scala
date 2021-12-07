import chisel3._
import chisel3.util._

class ArbiterIO[T <: Data](n: Int, private val gen: T) extends Bundle {
  val in = Flipped(Vec(n, new DecoupledIO(gen)))
  val out = new DecoupledIO(gen)
}

class Arbiter[T <: Data](n: Int, private val gen: T) extends Module {
  val io = IO(new ArbiterIO(n, gen))

  // incomplete function, just a placeholder
  def foo(a: DecoupledIO[T], b: DecoupledIO[T]) = {


    val regData = Reg(gen)
    val regValid = RegInit(false.B)
    val out = Wire(new DecoupledIO(gen))

    // Only one will be ready, as we cannot take two values
    // This would need a shadow register, a reasonable optimisation
    // Without optimisation one channel can only take one data every 2 clock cycles
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

    // the following should error, as this is the wrong direction
    // out.ready := a.ready

    out.bits := regData
    out.valid := regValid
    out
  }

  // io.out <> io.in.reduceTree(foo)
  io.out <> io.in.reduce(foo)
}

object Arbiter extends App {
  println((new chisel3.stage.ChiselStage).emitVerilog(new Arbiter(4, UInt(8.W))))
}
