import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class TimerSpec extends FlatSpec with Matchers {

  "Timer" should "pass" in {
    chisel3.iotesters.Driver(() => new Timer()) { c =>
      new PeekPokeTester(c) {

        // It starts with 0/done
        expect(c.io.done, 1)
        // no real load
        poke(c.io.din, 5)
        poke(c.io.load, 0)
        step(1)
        poke(c.io.din, 0)
        step(2)
        expect(c.io.done, 1)

        // load it
        poke(c.io.din, 5)
        poke(c.io.load, 1)
        step(1)
        poke(c.io.load, 0)
        step(2)
        expect(c.io.done, 0)
        step(3)
        expect(c.io.done, 1)
        step(2)
        expect(c.io.done, 1)
      }
    } should be (true)
  }

}

