package wildcat.pipeline

import chisel3._
import chisel3.util._
import wildcat.CSR._

/**
 * Control and Status Registers
 */
class Csr() extends Module {
  val io = IO(new Bundle {
    val address = Input(UInt(12.W))
    val data = Output(UInt(32.W))
  })

  val data = WireDefault(0.U(32.W))


  switch(io.address) {
    is(CYCLE.U) {
      data := 0.U
    }
    is(CYCLEH.U) {
      data := 0.U
    }
    is(MCYCLE.U) {
      data := 0.U
    }
    is(MCYCLEH.U) {
      data := 0.U
    }
    is(TIME.U) {
      data := 0.U
    }
    is(TIMEH.U) {
      data := 0.U
    }
    is(MTIME.U) {
      data := 0.U
    }
    is(MTIMEH.U) {
      data := 0.U
    }
    is(MARCHID.U) {
      data := WILDCAT_MARCHID.U
    }
  }

  io.data := data
}
