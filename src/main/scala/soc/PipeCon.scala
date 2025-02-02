package soc

import chisel3._

/**
 * A simple IO interface, as seen from the slave.
 * ack is used for acknowledgement in the following clock cycle, or later. (like OCPcore in Patmos).
 * Can be used to stall the CPU.
 *
 * @param addrWidth width of the address part (counting in bytes, maybe?)
 */
//- start pipe_con
class PipeCon(private val addrWidth: Int) extends Bundle {
  val address = Input(UInt(addrWidth.W))
  val rd = Input(Bool())
  val wr = Input(Bool())
  val rdData = Output(UInt(32.W))
  val wrData = Input(UInt(32.W))
  val wrMask = Input(UInt(4.W))
  val ack = Output(Bool())
}
//- end
