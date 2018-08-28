

import chisel3._
import chisel3.Driver

/**
 * Explore different register definitions.
 */

class DivReg extends Module {
  val io = IO(new Bundle {
    val led = Output(UInt(1.W))
  })
  val CNT_MAX = UInt(50000000 / 2 - 1);
  
  val cntReg = RegInit(UInt(0, 32.W))
  val cntReg1 = Reg(init = UInt(0, 32))
  val cntReg2 = Reg(init = UInt(0, 32.W))
  val cntReg3 = Reg(init = 0.asUInt(32.W))
  
  
  
  val blkReg = RegInit(UInt(0, 1.W))

  cntReg := cntReg + 1.U
  cntReg1 := cntReg1 + 1.U
  cntReg2 := cntReg2 + 1.U
  cntReg3 := cntReg3 + 1.U
  when(cntReg === CNT_MAX && cntReg1 === CNT_MAX &&
      cntReg2 === CNT_MAX && cntReg3 === CNT_MAX) {
    cntReg := 0.U
    blkReg := ~blkReg
  }
  io.led := blkReg
}

/**
 * An object containing a main() to invoke chiselMain()
 * to generate the Verilog code.
 */
object DivReg {
  def main(args: Array[String]): Unit = {
    chisel3.Driver.execute(Array("--target-dir", "generated"), () => new DivReg())
  }
}
