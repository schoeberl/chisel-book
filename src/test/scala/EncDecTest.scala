import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class EncDecTest extends AnyFlatSpec with ChiselScalatestTester {

  "EndDec" should "work" in {
    test(new EncDec()) { dut =>
      for (i <- 0 to 3) {
        dut.io.decin.poke(i.U)
        dut.io.encin.poke((1 << i).U)
        dut.clock.step(1)
        dut.io.decout.expect((1 << i).U)
        dut.io.encout.expect(i.U)
      }
    }
  }
  "Large encoder" should "work" in {
    test(new EncDec()) { dut =>
      for (i <- 0 to 15) {
        dut.io.largeEncIn.poke((1 << i).U)
        dut.clock.step()
        dut.io.largeEncOut.expect(i.U)
      }
    }
  }
}
