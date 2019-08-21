import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class CounterTester(c: Counter, n: Int) extends PeekPokeTester(c: Counter) {

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

  def getWhenCounter(n: Int): Counter = new WhenCounter(n)
  def getMuxCounter(n: Int): Counter = new MuxCounter(n)
  def getDownCounter(n: Int): Counter = new DownCounter(n)
  def getFunctionCounter(n: Int): Counter = new FunctionCounter(n)
  def getNerdCounter(n: Int): Counter = new NerdCounter(n)

  "WhenCounter 4" should "pass" in {
    chisel3.iotesters.Driver(() => getWhenCounter(4)) { c =>
      new CounterTester(c, 4)
    } should be (true)
  }

  "WhenCounter 7" should "pass" in {
    chisel3.iotesters.Driver(() => getWhenCounter(7)) { c =>
      new CounterTester(c, 7)
    } should be (true)
  }

  "MuxCounter 5" should "pass" in {
    chisel3.iotesters.Driver(() => getMuxCounter(5)) { c =>
      new CounterTester(c, 5)
    } should be (true)
  }

  "DownCounter 7" should "pass" in {
    chisel3.iotesters.Driver(() => getDownCounter(7)) { c =>
      new CounterTester(c, 7)
    } should be (true)
  }

  "FunctionCounter 8" should "pass" in {
    chisel3.iotesters.Driver(() => getFunctionCounter(8)) { c =>
      new CounterTester(c, 8)
    } should be (true)
  }

  "NerdCounter 13" should "pass" in {
    chisel3.iotesters.Driver(() => getNerdCounter(13)) { c =>
      new CounterTester(c, 13)
    } should be (true)
  }
}

