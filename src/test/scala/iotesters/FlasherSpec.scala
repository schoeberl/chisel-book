import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class FlasherBaseTester(f: FlasherBase) extends PeekPokeTester(f) {

  poke(f.io.start, 0)
  expect(f.io.light, 0)
  step(1)
  expect(f.io.light, 0)
  step(1)
  expect(f.io.light, 0)
  poke(f.io.start, 1)
  step(1)
  poke(f.io.start, 0)
  expect(f.io.light, 1)
  step(5)
  expect(f.io.light, 1)
  step(1)
  for (i <- 0 until 2) {
    expect(f.io.light, 0)
    step(3)
    expect(f.io.light, 0)
    step(1)
    expect(f.io.light, 1)
    step(5)
    expect(f.io.light, 1)
    step(1)
    expect(f.io.light, 0)
  }
  step(1)
  expect(f.io.light, 0)
  step(5)
  expect(f.io.light, 0)

}

class FlasherTester(f: Flasher) extends PeekPokeTester(f) {

  new FlasherBaseTester(f)
  // FIXME: even when Flasher2 fails, the test returns true
}

class Flasher2Tester(f: Flasher2) extends PeekPokeTester(f) {

  new FlasherBaseTester(f)
  // FIXME: even when Flasher2 fails, the test returns true
}

class FlasherSpec extends FlatSpec with Matchers {

  "Flasher" should "pass" in {
    chisel3.iotesters.Driver.execute(Array("--generate-vcd-output", "on"), () => new Flasher()) { dut =>
      new FlasherTester(dut)
    } should be (true)
  }

  "Flasher2" should "pass" in {
    chisel3.iotesters.Driver.execute(Array("--generate-vcd-output", "on"), () => new Flasher2()) { dut =>
      new Flasher2Tester(dut)
    } should be (true)
  }
}

