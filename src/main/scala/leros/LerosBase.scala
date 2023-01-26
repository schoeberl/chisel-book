/*
 * Leros, a Tiny Microprocessor
 *
 * Author: Martin Schoeberl (martin@jopdesign.com)
 */

package leros

import chisel3._

class Debug extends Bundle {
  val acc = Output(UInt())
  val pc = Output(UInt())
  val instr = Output(UInt())
  val exit = Output(Bool())
}

/**
  * Leros top level as abstract class.
  * Basically empty. Maybe we can/shall share code between different implementations.
  */
abstract class LerosBase(size: Int, memAddrWidth: Int, prog: String, fmaxReg: Boolean) extends Module {
  val io = IO(new Bundle {
    val dout = Output(UInt(32.W))
    val dbg = new Debug
  })

}


