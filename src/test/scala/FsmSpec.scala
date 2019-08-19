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

class FsmSpec extends FlatSpec with Matchers {
  "Fsm" should "pass" in {
    chisel3.iotesters.Driver.execute(Array(), () => new SimpleFsm()) { dut =>
      new SimpleFsmTester(dut)
    } should be (true)
  }
}
