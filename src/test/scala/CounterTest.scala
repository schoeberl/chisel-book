import chisel3._
import chiseltest._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

trait CountTest {
  def testFn[T <: Counter](c: T, n: Int) = {
    var count = -1
    for (_ <- 0 until n * 3) {
      if (count > 0)
        c.io.tick.expect(false.B)
      if (count == 0)
        c.io.tick.expect(true.B)
    
      if (c.io.tick.peekBoolean())
        count = n-1
      else
        count -= 1
      c.clock.step()
    }
  }
}

class CounterTest extends AnyFlatSpec with ChiselScalatestTester with CountTest {
  // `testFn` could have been defined here instead of in a separate trait.

  "WhenCounter 4" should "count" in {
    test(new WhenCounter(4)) { c => testFn(c, 4) }
  }

  "WhenCounter 7" should "count" in {
    test(new WhenCounter(7)) { c => testFn(c, 7) }
  }

  "MuxCounter 5" should "count" in {
    test(new MuxCounter(5)) { c => testFn(c, 5) }
  }

  "DownCounter 7" should "count" in {
    test(new DownCounter(7)) { c => testFn(c, 7) }
  }

  "FunctionCounter 8" should "count" in {
    test(new FunctionCounter(8)) { c => testFn(c, 8) }
  }

  "NerdCounter 13" should "count" in {
    test(new NerdCounter(13)) { c => testFn(c, 13) }
  }
}
