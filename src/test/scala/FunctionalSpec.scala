import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class FunctionalSpec extends FlatSpec with Matchers {

  "FunctionalAdd" should "pass" in {
    chisel3.iotesters.Driver(() => new FunctionalAdd()) { c =>
      new PeekPokeTester(c) {
        poke(c.io.in(0), 3)
        poke(c.io.in(1), 2)
        poke(c.io.in(2), 0)
        poke(c.io.in(3), 9)
        poke(c.io.in(4), 1)
        step(1)
        expect(c.io.res, 15)
      }
    } should be (true)
  }
}
