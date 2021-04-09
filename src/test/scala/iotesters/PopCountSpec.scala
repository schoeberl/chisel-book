import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class PopCountTester(c: PopCount) extends PeekPokeTester(c) {

  poke(c.io.din, 0xac)
  poke(c.io.dinValid, 1)
  step(9)
  expect(c.io.popCnt, 4)
}


class PopCountSpec extends FlatSpec with Matchers {

  "PopCount" should "pass" in {
    chisel3.iotesters.Driver(() => new PopCount()) { c =>
      new PopCountTester(c)
    } should be (true)
  }
}

