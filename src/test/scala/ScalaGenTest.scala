import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class ScalaGenTest extends AnyFlatSpec with ChiselScalatestTester {

  "Shift" should "work" in {
    test(new ScalaGen()) { dut =>
      dut.io.din.poke(1.U)
      dut.clock.step()
      dut.io.din.poke(0.U)
      dut.clock.step(7)
      dut.io.dout.expect("b10000000".U)
    }

  }
}
