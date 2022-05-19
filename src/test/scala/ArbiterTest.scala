import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class ArbiterTest extends AnyFlatSpec with ChiselScalatestTester {

  def check(i: Int) = {
    if ((i & 1) == 1) {
      "b001".U
    } else if ((i & 3) == 2) {
      "b010".U
    } else if (i == 4) {
      "b100".U
    } else {
      "b000".U
    }
  }

  "Arbiter3" should "pass" in {
    test(new Arbiter3()) { dut =>
      for (i <- 0 until 8) {
        dut.io.request.poke(i.U)
        dut.clock.step()
        dut.io.grant.expect(check(i))
      }
    }
  }

  "Arbiter3Direct" should "pass" in {
    test(new Arbiter3Direct()) { dut =>
      for (i <- 0 until 8) {
        dut.io.request.poke(i.U)
        dut.clock.step()
        dut.io.grant.expect(check(i))
      }
    }
  }

  "Arbiter3Loop" should "pass" in {
    test(new Arbiter3Loop()) { dut =>
      for (i <- 0 until 8) {
        dut.io.request.poke(i.U)
        dut.clock.step()
        dut.io.grant.expect(check(i))
      }
    }
  }
}
