//- start ticker_tester
import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class TickerTester[T <: Ticker](c: T, n: Int) extends PeekPokeTester(c: T) {

  var count = -1
  for (i <- 0 to n * 3) {
    if (count > 0) {
      expect(c.io.tick, 0)
    }
    if (count == 0) {
      expect(c.io.tick, 1)
    }
    val t = peek(c.io.tick)
    if (t == 1) {
      count = n-1
    } else {
      count -= 1
    }

    step(1)
  }
}
//- end


class TickerSpec extends FlatSpec with Matchers {

  "WhenTicker 5" should "pass" in {
    chisel3.iotesters.Driver(() => new WhenTicker(5)) { c =>
      new TickerTester(c, 5)
    } should be (true)
  }
}

