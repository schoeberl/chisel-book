//- start ticker_tester
import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

trait TickerTestFunc {
  def testFn[T <: Ticker](dut: T, n: Int) = {
    // -1 means that no ticks have been seen yet
    var count = -1
    for (_ <- 0 to n * 3) {
      // Check for correct output
      if (count > 0)
        dut.io.tick.expect(false.B)
      else if (count == 0)
        dut.io.tick.expect(true.B)
      
      // Reset the counter on a tick
      if (dut.io.tick.peekBoolean())
        count = n-1
      else
        count -= 1
      dut.clock.step()
    }
  }
}
//- end

//- start ticker_test
class TickerTest extends AnyFlatSpec with ChiselScalatestTester with TickerTestFunc {
  "UpTicker 5" should "pass" in {
    test(new UpTicker(5)) { dut => testFn(dut, 5) }
  }

  "DownTicker 7" should "pass" in {
    test(new DownTicker(7)) { dut => testFn(dut, 7) }
  }

  "NerdTicker 11" should "pass" in {
    test(new NerdTicker(11)) { dut => testFn(dut, 11) }
  }
}
//- end

