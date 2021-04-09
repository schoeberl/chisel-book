import chisel3.iotesters.PeekPokeTester
import org.scalatest.{FlatSpec, Matchers}

class SimpleFsmTester(dut: SimpleFsm) extends PeekPokeTester(dut) {

  poke(dut.io.clear, 0)
  poke(dut.io.badEvent, 0)
  expect(dut.io.ringBell, 0)
  step(1)
  expect(dut.io.ringBell, 0)
  poke(dut.io.badEvent, 1)
  step(1)
  expect(dut.io.ringBell, 0)
  poke(dut.io.badEvent, 0)
  step(1)
  expect(dut.io.ringBell, 0)
  poke(dut.io.badEvent, 1)
  step(1)
  expect(dut.io.ringBell, 1)
  poke(dut.io.badEvent, 0)
  step(1)
  expect(dut.io.ringBell, 1)
  poke(dut.io.clear, 1)
  step(1)
  expect(dut.io.ringBell, 0)
  // TODO: test if clar from orange works
}

class RisingFsmTester(dut: RisingFsm) extends PeekPokeTester(dut) {
  poke(dut.io.din, 0)
  expect(dut.io.risingEdge, 0)
  step(1)
  expect(dut.io.risingEdge, 0)
  step(1)
  poke(dut.io.din, 1)
  expect(dut.io.risingEdge, 1)
  step(1)
  expect(dut.io.risingEdge, 0)
  step(1)
  expect(dut.io.risingEdge, 0)
  poke(dut.io.din, 0)
  expect(dut.io.risingEdge, 0)
  step(1)
  expect(dut.io.risingEdge, 0)
}

class RisingMoreFsmTester(dut: RisingMooreFsm) extends PeekPokeTester(dut) {
  poke(dut.io.din, 0)
  expect(dut.io.risingEdge, 0)
  step(1)
  expect(dut.io.risingEdge, 0)
  step(1)
  poke(dut.io.din, 1)
  expect(dut.io.risingEdge, 0) // this would be mealy outpu
  step(1)
  expect(dut.io.risingEdge, 1) // this should be 1 in Moore
  step(1)
  expect(dut.io.risingEdge, 0)
  poke(dut.io.din, 0)
  expect(dut.io.risingEdge, 0)
  step(1)
  expect(dut.io.risingEdge, 0)
}

class FsmSpec extends FlatSpec with Matchers {
  "Fsm" should "pass" in {
    chisel3.iotesters.Driver.execute(Array(), () => new SimpleFsm()) { dut =>
      new SimpleFsmTester(dut)
    } should be (true)
  }

  "RisingFsm" should "pass" in {
    chisel3.iotesters.Driver.execute(Array(), () => new RisingFsm()) { dut =>
      new RisingFsmTester(dut)
    } should be (true)
  }

  "RisingMooreFsm" should "pass" in {
    chisel3.iotesters.Driver.execute(Array(), () => new RisingMooreFsm()) { dut =>
      new RisingMoreFsmTester(dut)
    } should be (true)
  }
}
