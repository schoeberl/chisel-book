import chisel3._
import chiseltest._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

class PopulationCountTest extends AnyFlatSpec with ChiselScalatestTester {
  "PopCount" should "pass" in {
    test(new PopulationCount) { dut =>
      dut.io.din.poke(0xac.U)
      dut.io.dinValid.poke(true.B)
      dut.clock.step(12)
      dut.io.popCnt.expect(4.U)
    }
  }
}
