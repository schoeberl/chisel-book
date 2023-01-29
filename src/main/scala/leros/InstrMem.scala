package leros

import chisel3._
import leros.util.Assembler



/**
 * Instruction memory.
 * Contains the register for using the on-chip ROM.
 * Uses Chisel synchronous reset to also execute the first instruction.
 *
 * FIXME: Verilog generation from Chisel results in logic, not in a ROM.
 */
//- start leros_instruction_memory
class InstrMem(memAddrWidth: Int, prog: String) extends Module {
  val io = IO(new Bundle {
    val addr = Input(UInt(memAddrWidth.W))
    val instr = Output(UInt(16.W))
  })
  val code = Assembler.getProgram(prog)
  assert(scala.math.pow(2, memAddrWidth) >= code.length, "Program too large")
  val progMem = VecInit(code.toIndexedSeq.map(_.asUInt(16.W)))
  val memReg = RegInit(0.U(memAddrWidth.W))
  memReg := io.addr
  io.instr := progMem(memReg)
}
//- end
