import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class RegisterTest extends AnyFlatSpec with ChiselScalatestTester {
  "Registers" should "store values" in {
    test(new Registers) { dut =>
      println(s"Register stores ${dut.io.out.peekInt()}")
      dut.io.in.poke(13.U)
      dut.clock.step()
      dut.io.out.expect(13.U)
      println(s"Register now stores ${dut.io.out.peekInt()}")
    }
  }
}
