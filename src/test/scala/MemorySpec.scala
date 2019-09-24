import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class MemoryTester(m: Memory) extends PeekPokeTester(m) {

  // fill the memory
  for (i <- 0 to 10) {
    poke(m.io.wrAddr, i)
    poke(m.io.wrData, i*10)
    poke(m.io.wrEna, 1)
    step(1)
  }
  poke(m.io.wrEna, 0)

  poke(m.io.rdAddr, 10)
  step(1)
  expect(m.io.rdData, 100)
  poke(m.io.rdAddr, 5)
  expect(m.io.rdData, 100)
  step(1)
  expect(m.io.rdData, 50)

  // same address read and write
  poke(m.io.wrAddr, 20)
  poke(m.io.wrData, 123)
  poke(m.io.wrEna, 1)
  poke(m.io.rdAddr, 20)
  step(1)
  println("Memory data: " + peek(m.io.rdData).toString())
}

class ForwardingMemoryTester(m: ForwardingMemory) extends PeekPokeTester(m) {

  // fill the memory
  for (i <- 0 to 10) {
    poke(m.io.wrAddr, i)
    poke(m.io.wrData, i*10)
    poke(m.io.wrEna, 1)
    step(1)
  }
  poke(m.io.wrEna, 0)

  poke(m.io.rdAddr, 10)
  step(1)
  expect(m.io.rdData, 100)
  poke(m.io.rdAddr, 5)
  expect(m.io.rdData, 100)
  step(1)
  expect(m.io.rdData, 50)

  // same address read and write
  poke(m.io.wrAddr, 20)
  poke(m.io.wrData, 123)
  poke(m.io.wrEna, 1)
  poke(m.io.rdAddr, 20)
  step(1)
  println("Memory data: " + peek(m.io.rdData).toString())
  expect(m.io.rdData, 123)
}

class MemorySpec extends FlatSpec with Matchers {

  "Memory" should "pass" in {
    chisel3.iotesters.Driver(() => new Memory()) { c =>
      new MemoryTester(c)
    } should be (true)
  }

  "Memory with forwarding" should "pass" in {
    chisel3.iotesters.Driver(() => new ForwardingMemory()) { c =>
      new ForwardingMemoryTester(c)
    } should be (true)
  }
}

