import chisel3._
import chisel3.util.PopCount

class CountBool extends Module {
  val io = IO(new Bundle() {
    val in = Input(Vec(9, Bool()))
    val out = Output(UInt(4.W))
  })

  io.out := PopCount(io.in)
}