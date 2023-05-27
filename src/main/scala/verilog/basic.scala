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

//- start sv_ch_adder
class ChiselAdder extends Module {
  val io = IO(new Bundle() {
    val a, b = Input(UInt(8.W))
    val sum = Output(UInt(8.W))
  })
  io.sum := io.a + io.b
}
//- end

class AdderTop extends Module {
  val io = IO(new Bundle() {
    val a, b = Input(UInt(8.W))
    val sum = Output(UInt(8.W))
    val c, d = Input(UInt(8.W))
    val s = Output(UInt(8.W))
  })

  val m = Module(new Adder)
  m.io.a := io.a
  m.io.b := io.b
  io.sum := m.io.sum
  val ch = Module(new ChiselAdder)
  ch.io.a := io.c
  ch.io.b := io.d
  io.s := ch.io.sum
}

class Register extends BlackBox with HasBlackBoxInline {
  val io = IO(new Bundle() {
    val clk = Input(Clock())
    val reset, enable = Input(UInt(1.W))
    val data = Input(UInt(8.W))
    val out = Output(UInt(8.W))
  })

  setInline("Register.sv",
    """
//- start sv_register
module Register(
      input wire clk,
      input wire reset,
      input wire enable,
      input wire [7:0] data,
      output wire [7:0] out
    );

  reg [7:0] reg_data;

  always @(posedge clk) begin
    if (reset)
      reg_data <= 0;
    else if (enable)
      reg_data <= data;
  end

  assign out = reg_data;
endmodule
//- end
""")}

//- start sv_ch_register
class ChiselRegister extends Module {
  val io = IO(new Bundle{
    val enable = Input(Bool())
    val data = Input(UInt(8.W))
    val out = Output(UInt(8.W))
  })

  io.out := RegEnable(io.data, 0.U(8.W), io.enable)
}
//- end

class RegisterTop extends Module {
  val io = IO(new Bundle() {
    val in = Input(UInt(8.W))
    val out = Output(UInt(8.W))
    val out2 = Output(UInt(8.W))
    val en = Input(UInt(1.W))
  })

  val m = Module(new Register)
  m.io.clk := clock
  m.io.reset := reset
  m.io.enable := io.en
  m.io.data := io.in
  io.out := m.io.out
  val cm = Module(new ChiselRegister)
  cm.io.enable := io.en
  cm.io.data := io.in
  io.out2 := cm.io.out
}
