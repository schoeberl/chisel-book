import chisel3._
import chiseltest._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

class ParamFuncTest extends AnyFlatSpec with ChiselScalatestTester {
  "ParamFunc" should "pass" in {
    test(new ParamFunc) { dut =>
      dut.io.selA.poke(true.B)
      dut.io.resA.expect(5.U)
      dut.io.selA.poke(false.B)
      dut.io.resA.expect(10.U)

      dut.io.selB.poke(true.B)
      dut.io.resB.b.expect(true.B)
      dut.io.resB.d.expect(42.U)
      dut.io.selB.poke(false.B)
      dut.io.resB.b.expect(false.B)
      dut.io.resB.d.expect(13.U)
    }
  }
}
