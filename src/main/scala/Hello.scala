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
import chisel3._
//- end

/**
 * The blinking LED component.
 */

//- start hello
class Hello extends Module {
  val io = IO(new Bundle {
    val led = Output(UInt(1.W))
  })
  val CNT_MAX = (50000000 / 2 - 1).U
  
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

// following does not produce a Verilog file
// chisel3.stage.ChiselStage.emitVerilog(new Hello())

/**
 * An object extending App to generate the Verilog code.
 */

//- start generate
object Hello extends App {
  emitVerilog(new Hello())
}
//- end

//- start generate_options
object HelloOption extends App {
  emitVerilog(new Hello(), Array("--target-dir", "generated"))
}
//- end

//- start generate_string
object HelloString extends App {
  val s = getVerilogString(new Hello())
  println(s)
}
//- end
