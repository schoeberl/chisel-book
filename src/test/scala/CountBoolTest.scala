import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class CountBoolTest extends AnyFlatSpec with ChiselScalatestTester {
  "CountBool" should "count them" in {
    test(new CountBool()) { dut =>
      for (i <- 0 to 8) {
        dut.io.in(i).poke(false.B)
      }
      dut.io.in(3).poke(true.B)
      dut.io.in(6).poke(true.B)
      dut.io.in(7).poke(true.B)
      dut.clock.step()
      dut.io.out.expect(3.U)
    }
  }
}