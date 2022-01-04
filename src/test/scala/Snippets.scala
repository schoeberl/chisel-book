import chisel3._
import chiseltest._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

class Snippets extends AnyFlatSpec with ChiselScalatestTester {
  class Dut extends Module {
    val io = IO(new Bundle{
      val in = Input(Bool())
      val out = Output(Bool())
    })
    io.out := !io.in
  }

  def testFun(dut: Dut) = {
    dut.io.in.poke(true.B)
    println(dut.io.out.peek())
    dut.io.out.expect(false.B)
    dut.io.in.poke(false.B)
    println(dut.io.out.peek())
    dut.io.out.expect(true.B)
  }

  "Combinational test with Treadle" should "pass" in {
    test(new Dut()) { c => testFun(c) }
  }

  "Combinational test with Verilator" should "pass" in {
    //- start verilator_anno
    test(new Dut()).withAnnotations(Seq(VerilatorBackendAnnotation)) {
          //- end
      c => testFun(c) 
    }
  }
}
