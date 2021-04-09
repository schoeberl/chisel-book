import chisel3._
import chisel3.util._
import chisel3.experimental.ExtModule

/*
 * For certain synthesis tools with built-in component libraries,
 * such as Xilinx' UNISIM library included in Vivado, it makes
 * sense to implement source-less blackboxes to instantiante such
 * primitives in one's generated Verilog. Beware that source-less
 * blackboxes can only be simulated in the vendors' simulators.
 * 
 * As examples, consider the Xilinx BUFGCE and the Intel/Altera
 * ALT_INBUF primitives below.
 * 
 * Beware that Verilog is case sensitive!
 * 
 * `BlackBox` modules are generated in the emitted Verilog, whereas
 * `ExtModule` modules are not.
 */
//- start xilinx_bufgce
class BUFGCE extends BlackBox(Map("SIM_DEVICE" -> "7SERIES")) {
  val io = IO(new Bundle {
    val I  = Input(Clock())
    val CE = Input(Bool())
    val O  = Output(Clock())
  })
}
//- end

//- start altera_alt_inbuf
class alt_inbuf extends ExtModule(Map("io_standard" -> "1.0 V",
                                      "location" -> "IOBANK_1",
                                      "enable_bus_hold" -> "on",
                                      "weak_pull_up_resistor" -> "off",
                                      "termination" -> "parallel 50 ohms")
                                      ) {
  val io = IO(new Bundle {
    val i = Input(Bool())
    val o = Output(Bool())
  })
}
//- end

/*
 * To force a specific implementation of a component, consider
 * manually writing the corresponding Verilog and including it
 * in a blackbox. This can aid synthesis tools in recognizing
 * specific code constructs and map them to device primivites,
 * e.g., LUTRAM on Xilinx FPGAs.
 * 
 * As examples, below are three versions of the same blackbox
 * adder component.
 * 
 * All three versions can be simulated with either Verilog-based
 * simulator backend; i.e., either Verilator or VCS, not Treadle.
 */
//- start blackbox_adder_io
class BlackBoxAdderIO extends Bundle {
  val a = Input(UInt(32.W))
  val b = Input(UInt(32.W))
  val cin = Input(Bool())
  val c = Output(UInt(32.W))
  val cout = Output(Bool())
}
//- end

// Generates the file in the test_run_dir subfolder if used for testing
// or in the --target-dir if emitted
//- start blackbox_adder_inline
class InlineBlackBoxAdder extends HasBlackBoxInline {
  val io = IO(new BlackBoxAdderIO)
  setInline("InlineBlackBoxAdder.v",
  s"""
  |module InlineBlackBoxAdder(a, b, cin, c, cout);
  |input  [31:0] a, b;
  |input  cin;
  |output [31:0] c;
  |output cout;
  |wire   [32:0] sum;
  |
  |assign sum  = a + b + cin;
  |assign c    = sum[31:0];
  |assign cout = sum[32];
  |
  |endmodule
  """.stripMargin)
}
//- end

// Accepts relative paths w.r.t. the project directory
//- start blackbox_adder_path
class PathBlackBoxAdder extends HasBlackBoxPath {
  val io = IO(new BlackBoxAdderIO)
  addPath("./src/main/resources/PathBlackBoxAdder.v")
}
//- end

// Expects source file to be present in {project-dir}/src/main/resources
//- start blackbox_adder_resource
class ResourceBlackBoxAdder extends HasBlackBoxResource {
  val io = IO(new BlackBoxAdderIO)
  addResource("/ResourceBlackBoxAdder.v")
}
//- end
