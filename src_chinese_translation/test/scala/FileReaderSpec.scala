import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class FileReaderTester(f: FileReader) extends PeekPokeTester(f) {

  poke(f.io.address, 0)
  expect(f.io.data, 123)
  poke(f.io.address, 1)
  expect(f.io.data, 255)
  poke(f.io.address, 2)
  expect(f.io.data, 0)
  poke(f.io.address, 3)
  expect(f.io.data, 1)
  poke(f.io.address, 4)
  expect(f.io.data, 0)
}

class FileReaderSpec extends FlatSpec with Matchers {

  "FileReader" should "pass" in {
    chisel3.iotesters.Driver(() => new FileReader()) { c =>
      new FileReaderTester(c)
    } should be (true)
  }
}

