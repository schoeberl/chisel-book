import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class ParamFuncTester(c: ParamFunc) extends PeekPokeTester(c) {

  poke(c.io.selA, 1)
  expect(c.io.resA, 5)
  poke(c.io.selA, 0)
  expect(c.io.resA, 10)

  poke(c.io.selB, 1)
  expect(c.io.resB.b, 1)
  expect(c.io.resB.d, 42)
  poke(c.io.selB, 0)
  expect(c.io.resB.b, 0)
  expect(c.io.resB.d, 13)
}

class ParamFuncSpec extends FlatSpec with Matchers {

  "ParamFunc" should "pass" in {
    chisel3.iotesters.Driver(() => new ParamFunc()) { c =>
      new ParamFuncTester(c)
    } should be (true)
  }
}

