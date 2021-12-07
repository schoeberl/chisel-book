import chisel3._
import chiseltest._
import org.scalatest.FlatSpec

class ArbiterTest extends FlatSpec with ChiselScalatestTester {

  behavior of "Arbiter"

  it should "pass" in {
    test(new Arbiter(8, UInt(8.W))) { dut =>
        dut.io.in(0).valid.poke(true.B)
    }
  }
}
