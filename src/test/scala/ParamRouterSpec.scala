import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class ParamRouterTester(c: NocRouter[Payload]) extends PeekPokeTester(c) {

  poke(c.io.inPort(0).data, 123)
  poke(c.io.inPort(1).data, 42)
  expect(c.io.outPort(1).data, 123)
  expect(c.io.outPort(0).data, 42)
}

class ParamRouterSpec extends FlatSpec with Matchers {

  "ParamRouter" should "pass" in {
    chisel3.iotesters.Driver(() => new NocRouter[Payload](new Payload, 2)) { c =>
      new ParamRouterTester(c)
    } should be (true)
  }
}

