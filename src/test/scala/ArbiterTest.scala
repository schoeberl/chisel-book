import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class ArbiterTest extends AnyFlatSpec with ChiselScalatestTester {

  "Arbiter3" should "pass" in {
    test(new Arbiter3()) { dut =>
      for (i <- 0 until 8) {
        println(s"$i")
        dut.io.request.poke(i.U)
        dut.clock.step()
        if ((i & 1) == 1) {
          dut.io.grant.expect("b001".U)
        } else if ((i & 3) == 2) {
          dut.io.grant.expect("b010".U)
        } else if (i == 4) {
          dut.io.grant.expect("b100".U)
        } else {
          dut.io.grant.expect("b000".U)
        }
      }
    }
  }
}
