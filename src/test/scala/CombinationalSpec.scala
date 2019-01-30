import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class CombinationalTester(c: Combinational) extends PeekPokeTester(c) {

  poke(c.io.a, 0xc)
  poke(c.io.b, 0xa)
  poke(c.io.c, 0x1)
  expect(c.io.out, 0x9)
  expect(c.io.out2, 0x6)

  poke(c.io.a, 0x3)
  poke(c.io.b, 0x2)
  poke(c.io.c, 0xc)
  expect(c.io.out, 0xe)
  expect(c.io.out2, 0x1)
}

class CombWhenTester(c: CombWhen) extends PeekPokeTester(c) {
  poke(c.io.cond, 0)
  expect(c.io.out, 0)
  poke(c.io.cond, 1)
  expect(c.io.out, 3)
}

class CombOtherTester(c: CombOther) extends PeekPokeTester(c) {
  poke(c.io.cond, 0)
  expect(c.io.out, 2)
  poke(c.io.cond, 1)
  expect(c.io.out, 1)
}

class CombElseWhenTester(c: CombElseWhen) extends PeekPokeTester(c) {
  poke(c.io.cond, 0)
  poke(c.io.cond2, 0)
  expect(c.io.out, 3)
  poke(c.io.cond, 1)
  poke(c.io.cond2, 0)
  expect(c.io.out, 1)
  poke(c.io.cond, 0)
  poke(c.io.cond2, 1)
  expect(c.io.out, 2)
  poke(c.io.cond, 1)
  poke(c.io.cond2, 1)
  expect(c.io.out, 1)
}

class CombWireDefaultTester(c: CombWireDefault) extends PeekPokeTester(c) {
  poke(c.io.cond, 0)
  expect(c.io.out, 0)
  poke(c.io.cond, 1)
  expect(c.io.out, 3)
}

class CombinationalSpec extends FlatSpec with Matchers {

  "Combinational" should "pass" in {
    chisel3.iotesters.Driver(() => new Combinational()) { c =>
      new CombinationalTester(c)
    } should be (true)
  }

  "CombWhen" should "pass" in {
    chisel3.iotesters.Driver(() => new CombWhen()) { c =>
      new CombWhenTester(c)
    } should be (true)
  }

  "CombOther" should "pass" in {
    chisel3.iotesters.Driver(() => new CombOther()) { c =>
      new CombOtherTester(c)
    } should be (true)
  }

  "CombElseWhen" should "pass" in {
    chisel3.iotesters.Driver(() => new CombElseWhen()) { c =>
      new CombElseWhenTester(c)
    } should be (true)
  }

  "CombWireDefault" should "pass" in {
    chisel3.iotesters.Driver(() => new CombWireDefault()) { c =>
      new CombWireDefaultTester(c)
    } should be (true)
  }
}

