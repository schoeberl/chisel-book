package verilog

import chisel3._
import chisel3.util._
class Simple extends BlackBox with HasBlackBoxInline {
  val io = IO(new Bundle() {
    val a, b = Input(UInt(8.W))
    val c = Output(UInt(8.W))
  })

  setInline("Simple.sv",
    """module Simple(
          input  [7:0] a,
          input  [7:0] b,
          output reg [7:0] c
      );
//- start sv_first
always_comb begin
  c = a + b;
end
//- end
      endmodule
  """)
}

class SimpleTop extends Module {
  val io = IO(new Bundle() {
    val a, b = Input(UInt(8.W))
    val c = Output(UInt(8.W))
  })

  val m = Module(new Simple)
  m.io <> io
}

object Simple extends App {
  println(getVerilogString(new SimpleTop()))
}
