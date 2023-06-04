import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class AssertTest extends AnyFlatSpec with ChiselScalatestTester {

  "Assert" should "work" in {
    test(new Assert()) { dut =>
      dut.io.a.poke(1.U)
      dut.io.b.poke(2.U)
      dut.clock.step()
      // dut.io.sum.expect(1.U)
      dut.io.a.poke(100.U)
      dut.io.b.poke(200.U)
      dut.clock.step()
    }
  }
}
