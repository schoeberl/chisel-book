import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class TimerTest extends AnyFlatSpec with ChiselScalatestTester {
  "Timer" should "pass" in {
    test(new Timer) { dut =>
      // Timer should start at 0
      dut.io.done.expect(true.B)
      dut.io.din.poke(5.U)
      dut.io.load.poke(false.B)
      dut.clock.step()
      dut.io.din.poke(0.U)
      dut.clock.step(2)
      dut.io.done.expect(true.B)

      // Load the timer
      dut.io.din.poke(5.U)
      dut.io.load.poke(true.B)
      dut.clock.step()
      dut.io.load.poke(false.B)
      dut.clock.step(2)
      dut.io.done.expect(false.B)
      dut.clock.step(3)
      dut.io.done.expect(true.B)
      dut.clock.step(2)
      dut.io.done.expect(true.B)
    }
  }
}
