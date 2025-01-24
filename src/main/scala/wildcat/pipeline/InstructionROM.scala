package wildcat.pipeline

import chisel3._

/**
 * On-chip memory with one clock cycle read timing, preloaded on construction.
 */
class InstructionROM(code: Array[Int]) extends Module {
  val io = IO(Flipped(new InstrIO()))

  val addrReg = RegInit(0.U(32.W))
  addrReg := io.address
  val instructions = VecInit(code.toIndexedSeq.map(_.S(32.W).asUInt))
  io.data := instructions(addrReg(31, 2))
  // for checking two failing tests
  val toggle = RegInit(false.B)
  toggle := !toggle
  io.stall := false.B
}
