import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class BcdTableTester(f: BcdTable) extends PeekPokeTester(f) {

  poke(f.io.address, 0)
  expect(f.io.data, 0)
  poke(f.io.address, 1)
  expect(f.io.data, 1)
  poke(f.io.address, 13)
  expect(f.io.data, 0x13)
  poke(f.io.address, 99)
  expect(f.io.data, 0x99)

  // New table goes only till 99
  // poke(f.io.address, 100)
  // expect(f.io.data, 0)
}

class BcdTableSpec extends FlatSpec with Matchers {

  "BcdTable" should "pass" in {
    chisel3.iotesters.Driver(() => new BcdTable()) { c =>
      new BcdTableTester(c)
    } should be (true)
  }
}

