import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class ShiftRegisterSpec extends FlatSpec with Matchers {

  "ShiftRegister" should "pass" in {
    chisel3.iotesters.Driver(() => new ShiftRegister()) { c =>
      new PeekPokeTester(c) {
        poke(c.io.din, 1)
        step(1)
        poke(c.io.din, 0)
        step(3)
        expect(c.io.dout, 1)
        step(1)
        expect(c.io.dout, 0)
        step(1)
        expect(c.io.dout, 0)

      }
    } should be (true)
  }

  "ShiftRegisterParaOut" should "pass" in {
    chisel3.iotesters.Driver(() => new ShiftRegister()) { c =>
      new PeekPokeTester(c) {
        poke(c.io.serIn, 1)
        step(1)
        poke(c.io.serIn, 0)
        step(1)
        poke(c.io.serIn, 0)
        step(1)
        poke(c.io.serIn, 1)
        step(1)

        expect(c.io.paraOut, 0x9)
      }
    } should be (true)
  }

  "ShiftRegisterParaLoad" should "pass" in {
    chisel3.iotesters.Driver(() => new ShiftRegister()) { c =>
      new PeekPokeTester(c) {
        poke(c.io.d, 0)
        poke(c.io.load, 0)
        step(1)
        poke(c.io.d, 0xa)
        poke(c.io.load, 1)
        step(1)
        poke(c.io.d, 0)
        poke(c.io.load, 0)
        expect(c.io.serOut, 0)
        step(1)
        expect(c.io.serOut, 1)
        step(1)
        expect(c.io.serOut, 0)
        step(1)
        expect(c.io.serOut, 1)
      }
    } should be (true)
  }
}

