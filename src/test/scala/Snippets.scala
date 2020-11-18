import chisel3._
import chiseltest._
import chiseltest.experimental.TestOptionBuilder._
import chiseltest.internal.VerilatorBackendAnnotation
import org.scalatest._

class Snippets extends FlatSpec with ChiselScalatestTester with Matchers {

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
    dut.io.in.poke(false.B)
    println(dut.io.out.peek())
  }

  "Combinational test with Treadle" should "pass" in {
    test(new Dut()) { c => testFun(c)}
  }

  "Combinational test with Verilator" should "pass" in {
    test(new Dut()).withAnnotations(Seq(VerilatorBackendAnnotation)) { c => testFun(c)}
  }

}
