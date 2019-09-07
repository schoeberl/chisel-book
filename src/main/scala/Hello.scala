/*
 * This code is a minimal hardware described in Chisel.
 * 
 * Copyright: 2013, Technical University of Denmark, DTU Compute
 * Author: Martin Schoeberl (martin@jopdesign.com)
 * License: Simplified BSD License
 * 
 * Blinking LED: the FPGA version of Hello World
 */

//- start import
//- 开始引用
import chisel3._
//- end
//- 结束

/**
 * The blinking LED component.
 * 闪烁的二极管部分
 */

//- start hello
//- 开始hello
class Hello extends Module {
  val io = IO(new Bundle {
    val led = Output(UInt(1.W))
  })
  val CNT_MAX = (50000000 / 2 - 1).U;
  
  val cntReg = RegInit(0.U(32.W))
  val blkReg = RegInit(0.U(1.W))

  cntReg := cntReg + 1.U
  when(cntReg === CNT_MAX) {
    cntReg := 0.U
    blkReg := ~blkReg
  }
  io.led := blkReg
}
//- end
//- 结束

/**
 * An object extending App to generate the Verilog code.
 * 一个拓展性的App生成Verilog代码
 */
//- start generate
//- 开始生成
object Hello extends App {
  chisel3.Driver.execute(Array[String](), () => new Hello())
}
//- end
//- 结束
