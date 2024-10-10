package verilog

//- start v_ch_adder
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

  //- start v_use_chisel_adder
  val in1 = Wire(UInt(8.W))
  val in2 = Wire(UInt(8.W))
  val result = Wire(UInt(8.W))

  val m = Module(new ChiselAdder)
  m.io.a := in1
  m.io.b := in2
  result := m.io.sum
  //- end

  in1 := io.a
  in2 := io.b
  io.sum := result
}
class adder extends BlackBox with HasBlackBoxInline {
  val io = IO(new Bundle() {
    val a, b = Input(UInt(8.W))
    val sum = Output(UInt(8.W))
  })

  setInline("adder.v",
    """
//- start v_adder
module adder(
  input  [7:0] a,
  input  [7:0] b,
  output [7:0] sum
);

  assign sum = a + b;

endmodule
//- end
  """)
}

class UseAdder extends BlackBox with HasBlackBoxInline {
  val io = IO(new Bundle() {
    val a, b = Input(UInt(8.W))
    val sum = Output(UInt(8.W))
  })

  setInline("UseAdder.v",
    """
//- start v_adder
module adder(
  input  [7:0] a,
  input  [7:0] b,
  output [7:0] sum
);

  assign sum = a + b;

endmodule
//- end

module UseAdder(
      input  [7:0] a,
      input  [7:0] b,
      output [7:0] sum
    );

//- start v_use_adder
    wire [7:0] in1;
    wire [7:0] in2;
    wire [7:0] result;

    adder m(.a(in1), .b(in2), .sum(result));
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

  setInline("Register.v",
    """
module Register(
      input wire clk,
      input wire reset,
      input wire enable,
      input wire [7:0] data,
      output wire [7:0] out
    );

//- start v_register
  reg [7:0] reg_data;

  always @(posedge clk) begin
    if (reset)
      reg_data <= 8'b0;
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
  //- start v_ch_register
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

class comb extends BlackBox with HasBlackBoxInline {
  val io = IO(new Bundle() {
    val sel = Input(UInt(2.W))
    val in = Input(UInt(4.W))
    val out = Output(UInt(1.W))
  })

  setInline("comb.v",
    """
//- start v_comb
module comb(
  input  [1:0] sel,
  input  [3:0] in,
  output reg out
);

  always @(*) begin
    case (sel)
      2'b00: out = in[0];
      2'b01: out = in[1];
      2'b10: out = in[2];
      2'b11: out = in[3];
      default: out = 1'b0;
    endcase
  end

endmodule
//- end
""")}

class ChiselComb extends Module {
  val io = IO(new Bundle() {
    val sel = Input(UInt(2.W))
    val in = Input(UInt(4.W))
    val out = Output(UInt(1.W))
  })
  //- start v_ch_comb
  io.out := 0.U
  switch(io.sel) {
    is("b00".U) { io.out := io.in(0) }
    is("b01".U) { io.out := io.in(1) }
    is("b10".U) { io.out := io.in(2) }
    is("b11".U) { io.out := io.in(3) }
  }
  //- end
}

class CombTop extends Module {
  val io = IO(new Bundle() {
    val sel = Input(UInt(2.W))
    val in = Input(UInt(4.W))
    val out = Output(UInt(1.W))
    val out2 = Output(UInt(1.W))
  })

  val m = Module(new comb)
  m.io.sel := io.sel
  m.io.in := io.in
  io.out := m.io.out
  val cm = Module(new ChiselComb)
  cm.io.sel := io.sel
  cm.io.in := io.in
  io.out2 := cm.io.out
}

class ChiselIfElse extends Module {
  val io = IO(new Bundle() {
    val c1 = Input(Bool())
    val c2 = Input(Bool())
    val in1 = Input(UInt(8.W))
    val in2 = Input(UInt(8.W))
    val in3 = Input(UInt(8.W))
    val out = Output(UInt(8.W))
  })
  //- start v_ch_if_else
  when (io.c1) {
    io.out := io.in1
  } .elsewhen (io.c2) {
    io.out := io.in2
  } .otherwise {
    io.out := io.in3
  }
  //- end
}

class if_else extends BlackBox with HasBlackBoxInline {
  val io = IO(new Bundle() {
    val c1 = Input(Bool())
    val c2 = Input(Bool())
    val in1 = Input(UInt(8.W))
    val in2 = Input(UInt(8.W))
    val in3 = Input(UInt(8.W))
    val out = Output(UInt(8.W))
  })
  setInline("if_else.v",
    """
module if_else(
  input wire c1,
  input wire c2,
  input wire [7:0] in1,
  input wire [7:0] in2,
  input wire [7:0] in3,
  output reg [7:0] out
);
//- start v_if_else
  always @(*) begin
    if (c1)
      out = in1;
    else if (c2)
      out = in2;
    else
      out = in3;
  end
//- end

endmodule
""")}
class IfElseTop extends Module {
  val io = IO(new Bundle() {
    val c1 = Input(Bool())
    val c2 = Input(Bool())
    val in1 = Input(UInt(8.W))
    val in2 = Input(UInt(8.W))
    val in3 = Input(UInt(8.W))
    val out = Output(UInt(8.W))
    val out2 = Output(UInt(8.W))
  })
  val cm = Module(new ChiselIfElse)
  cm.io.c1 := io.c1
  cm.io.c2 := io.c2
  cm.io.in1 := io.in1
  cm.io.in2 := io.in2
  cm.io.in3 := io.in3
  io.out := cm.io.out

  val m = Module(new if_else)
  m.io.c1 := io.c1
  m.io.c2 := io.c2
  m.io.in1 := io.in1
  m.io.in2 := io.in2
  m.io.in3 := io.in3
  io.out2 := m.io.out
}

