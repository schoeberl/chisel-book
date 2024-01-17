package verilog

//- start sv_ch_adder
import chisel3._
import chisel3.util._

class ChiselAdder extends Module {
  val io = IO(new Bundle() {
    val a, b = Input(UInt(8.W))
    val sum = Output(UInt(8.W))
  })
  io.sum := io.a + io.b
}
//- end

class UseChiselAdder extends Module {
  val io = IO(new Bundle() {
    val a, b = Input(UInt(8.W))
    val sum = Output(UInt(8.W))
  })

  //- start sv_use_chisel_adder
  val in1 = Wire(UInt(8.W))
  val in2 = Wire(UInt(8.W))
  val result = Wire(UInt(8.W))

  val m = Module(new Adder)
  m.io.a := in1
  m.io.b := in2
  result := m.io.sum
  //- end

  in1 := io.a
  in2 := io.b
  io.sum := result
}
class Adder extends BlackBox with HasBlackBoxInline {
  val io = IO(new Bundle() {
    val a, b = Input(UInt(8.W))
    val sum = Output(UInt(8.W))
  })

  setInline("Adder.sv",
    """
//- start sv_adder
module AdderX(
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

class UseAdder extends BlackBox with HasBlackBoxInline {
  val io = IO(new Bundle() {
    val a, b = Input(UInt(8.W))
    val sum = Output(UInt(8.W))
  })

  setInline("UseAdder.sv",
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

module UseAdder(
      input  [7:0] a,
      input  [7:0] b,
      output reg [7:0] sum
    );

//- start sv_use_adder
    wire [7:0] in1;
    wire [7:0] in2;
    wire [7:0] result;

    Adder m(.a(in1), .b(in2), .sum(result));
//- end

    assign in1 = a;
    assign in2 = b;

    assign sum = result;
endmodule
""")
}

class AdderTop extends Module {
  val io = IO(new Bundle() {
    val a, b = Input(UInt(8.W))
    val sum = Output(UInt(8.W))
    val c, d = Input(UInt(8.W))
    val s = Output(UInt(8.W))
  })

  val m = Module(new UseAdder)
  m.io.a := io.a
  m.io.b := io.b
  io.sum := m.io.sum
  val ch = Module(new UseChiselAdder)
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
module Register(
      input wire clk,
      input wire reset,
      input wire enable,
      input wire [7:0] data,
      output wire [7:0] out
    );

//- start sv_register
  reg [7:0] reg_data;

  always_ff @(posedge clk) begin
    if (reset)
      reg_data <= 0;
    else if (enable)
      reg_data <= data;
  end
//- end

  assign out = reg_data;
endmodule
""")}

class ChiselRegister extends Module {
  val io = IO(new Bundle{
    val enable = Input(Bool())
    val data = Input(UInt(8.W))
    val out = Output(UInt(8.W))
  })

  val data = io.data
  val enable = io.enable
  //- start sv_ch_register
  val reg = RegEnable(data, 0.U(8.W), enable)
  //- end
  io.out := reg
}

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
