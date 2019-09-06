import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class CounterTester[T <: Counter](c: T, n: Int) extends PeekPokeTester(c: T) {

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


class CounterSpec extends FlatSpec with Matchers {

  "WhenCounter 4" should "pass" in {
    chisel3.iotesters.Driver(() => new WhenCounter(4)) { c =>
      new CounterTester(c, 4)
    } should be (true)
  }

  "WhenCounter 7" should "pass" in {
    chisel3.iotesters.Driver(() => new WhenCounter(7)) { c =>
      new CounterTester(c, 7)
    } should be (true)
  }

  "MuxCounter 5" should "pass" in {
    chisel3.iotesters.Driver(() => new MuxCounter(5)) { c =>
      new CounterTester(c, 5)
    } should be (true)
  }

  "DownCounter 7" should "pass" in {
    chisel3.iotesters.Driver(() => new DownCounter(7)) { c =>
      new CounterTester(c, 7)
    } should be (true)
  }

  "FunctionCounter 8" should "pass" in {
    chisel3.iotesters.Driver(() => new FunctionCounter(8)) { c =>
      new CounterTester(c, 8)
    } should be (true)
  }

  "NerdCounter 13" should "pass" in {
    chisel3.iotesters.Driver(() => new NerdCounter(13)) { c =>
      new CounterTester(c, 13)
    } should be (true)
  }
}

