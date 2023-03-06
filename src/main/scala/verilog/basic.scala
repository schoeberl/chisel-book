package verilog

import chisel3._
import chisel3.util._
class Adder extends BlackBox with HasBlackBoxInline {
  val io = IO(new Bundle() {
    val a, b = Input(UInt(8.W))
    val sum = Output(UInt(8.W))
  })

  setInline("Adder.sv",
    """
//- start sv_adder
module Adder(
  input  [7:0] a,
  input  [7:0] b,
  output reg [7:0] sum
);

  always_comb begin
    sum = a + b;
  end
endmodule
//- end
  """)
}

class AdderTop extends Module {
  val io = IO(new Bundle() {
    val a, b = Input(UInt(8.W))
    val sum = Output(UInt(8.W))
  })

  val m = Module(new Adder)
  m.io <> io
}

object Adder extends App {
  println(getVerilogString(new AdderTop()))
}
