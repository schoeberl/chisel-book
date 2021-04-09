import chisel3._
import chiseltest._
import org.scalatest._

class PopCountTest extends FlatSpec with ChiselScalatestTester {
  "PopCount" should "pass" in {
    test(new PopCount) { dut =>
      dut.io.din.poke(0xac.U)
      dut.io.dinValid.poke(true.B)
      dut.clock.step(9)
      dut.io.popCnt.expect(4.U)
    }
  }
}
