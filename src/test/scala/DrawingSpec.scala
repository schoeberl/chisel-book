import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class DrawingSpec extends FlatSpec with Matchers {

  "DrawAcc" should "pass" in {
    chisel3.iotesters.Driver(() => new DrawAcc()) { c =>
      new PeekPokeTester(c) {
        poke(c.din, 3)
        poke(c.sel, 0)
        step(1)
        expect(c.io.dout, 0)
        poke(c.sel, 2)
        step(1)
        expect(c.io.dout, 3)
        step(1)
        expect(c.io.dout, 6)
        poke(c.sel, 1)
        step(1)
        expect(c.io.dout, 0)
      }
    } should be (true)
  }

  "DrawMux6" should "pass" in {
    chisel3.iotesters.Driver(() => new DrawMux6()) { c =>
      new PeekPokeTester(c) {
        poke(c.sel, 0)
        step(1)
        expect(c.io.dout, 0)
        poke(c.sel, 1)
        step(1)
        expect(c.io.dout, 11)
        poke(c.sel, 5)
        step(1)
        expect(c.io.dout, 55)
      }
    } should be (true)
  }

}
