import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class BundleVecTester(b: BundleVec) extends PeekPokeTester(b) {

  expect(b.io.chan.data, 123)
  expect(b.io.chan.valid, 1)
  expect(b.io.chan2.data, 123)

  poke(b.io.idx, 0)
  expect(b.io.array, 1)
  poke(b.io.idx, 1)
  expect(b.io.array, 3)
  poke(b.io.idx, 2)
  expect(b.io.array, 5)

  expect(b.io.bvOut, 13)

  expect(b.io.chreg.valid, 0)
  expect(b.io.chreg.data, 0)
  poke(b.io.idx, 2)
  poke(b.io.dIn, 5)
  step(1)
  expect(b.io.chreg.valid, 1)
  expect(b.io.chreg.data, 1)
  poke(b.io.idx, 2)
  poke(b.io.dIn, 7)
  expect(b.io.dOut, 5)
  step(1)
  expect(b.io.dOut, 7)
}

class BundleVecSpec extends FlatSpec with Matchers {

  "BundleVec" should "pass" in {
    chisel3.iotesters.Driver(() => new BundleVec()) { c =>
      new BundleVecTester(c)
    } should be (true)
  }
}

