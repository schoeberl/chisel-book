import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class ComponentFnTester(c: CompFn) extends PeekPokeTester(c) {

  poke(c.io.a, 0x1)
  poke(c.io.b, 0x2)
  expect(c.io.out, 0x3)
  poke(c.io.c, 5)
  poke(c.io.d, 6)
  expect(c.io.out2, 11-1)

  // two tap delay
  poke(c.io.del, 5)
  step(5)
  expect(c.io.out3, 5)
  poke(c.io.del, 7)
  step(1)
  expect(c.io.out3, 5)
  step(1)
  expect(c.io.out3, 7)


}

class ComponentSpec extends FlatSpec with Matchers {

  "ComponentFn" should "pass" in {
    chisel3.iotesters.Driver(() => new CompFn()) { c =>
      new ComponentFnTester(c)
    } should be (true)
  }

  "Count10" should "pass" in {
    chisel3.iotesters.Driver(() => new Count10()) { c =>
      new PeekPokeTester(c) {
        expect(c.io.dout, 0)
        step(1)
        expect(c.io.dout, 1)
        step(1)
        expect(c.io.dout, 2)
        step(1)
        expect(c.io.dout, 3)
        step(1)
        expect(c.io.dout, 4)
        step(5)
        expect(c.io.dout, 9)
        step(1)
        expect(c.io.dout, 0)
      }
    } should be (true)
  }
}

