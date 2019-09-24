import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class SequentialTester(c: Sequential) extends PeekPokeTester(c) {

  poke(c.io.d, 0xc)
  poke(c.io.d2, 0xa)
  poke(c.io.d3, 0xf)
  poke(c.io.ena, 1)
  expect(c.io.q3, 0x0)
  expect(c.io.q5, 0x0)
  poke(c.io.riseIn, 0)
  step(1)
  poke(c.io.d, 0x1)
  poke(c.io.d2, 0x2)
  poke(c.io.d3, 0x3)
  poke(c.io.ena, 0)
  expect(c.io.q, 0xc)
  expect(c.io.q2, 0xa)
  expect(c.io.q3, 0xf)
  expect(c.io.q4, 0xf)
  expect(c.io.q5, 0xf)
  poke(c.io.riseIn, 1)
  expect(c.io.riseOut, 1)
  step(1)
  expect(c.io.q, 0x1)
  expect(c.io.q2, 0x2)
  expect(c.io.q3, 0x3)
  expect(c.io.q4, 0xf)
  expect(c.io.q5, 0xf)
  poke(c.io.ena, 1)
  expect(c.io.riseOut, 0)
  step(1)
  expect(c.io.q4, 0x3)
  expect(c.io.q5, 0x3)
  expect(c.io.riseOut, 0)

}

class SequCounterTester(c: SequCounter) extends PeekPokeTester(c) {

  expect(c.io.out, 0)
  expect(c.io.eventCnt, 0)
  poke(c.io.event, 0)
  step(1)
  expect(c.io.out, 1)
  expect(c.io.eventCnt, 0)
  poke(c.io.event, 1)
  step(1)
  expect(c.io.out, 2)
  expect(c.io.eventCnt, 1)
  poke(c.io.event, 0)
  step(15)
  expect(c.io.out, 1)
  expect(c.io.eventCnt, 1)
}


class SequTickTester(c: SequCounter) extends PeekPokeTester(c) {

  expect(c.io.tick, 0)
  expect(c.io.lowCnt, 0)
  step(1)
  expect(c.io.tick, 0)
  step(1)
  expect(c.io.tick, 0)
  step(1)
  expect(c.io.tick, 0)
  expect(c.io.lowCnt, 0)
  step(1)
  expect(c.io.tick, 1)
  expect(c.io.lowCnt, 0)
  step(1)
  expect(c.io.tick, 0)
  expect(c.io.lowCnt, 1)
  step(4)
  expect(c.io.lowCnt, 1)
  expect(c.io.tick, 1)
  step(1)
  expect(c.io.tick, 0)
  expect(c.io.lowCnt, 2)
}


class SequentialSpec extends FlatSpec with Matchers {

  "Sequential" should "pass" in {
    chisel3.iotesters.Driver(() => new Sequential()) { c =>
      new SequentialTester(c)
    } should be (true)
  }

  "Counter" should "pass" in {
    chisel3.iotesters.Driver(() => new SequCounter()) { c =>
      new SequCounterTester(c)
    } should be (true)
  }

  "Tick" should "pass" in {
    chisel3.iotesters.Driver(() => new SequCounter()) { c =>
      new SequTickTester(c)
    } should be (true)
  }
}

