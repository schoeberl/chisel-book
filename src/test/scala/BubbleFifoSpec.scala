import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class FifoTester(dut: BubbleFifo) extends PeekPokeTester(dut) {

  // some defaults for all signals
  poke(dut.io.enq.din, 0xab)
  poke(dut.io.enq.write, 0)
  poke(dut.io.deq.read, 0)
  step(1)
  var full = peek(dut.io.enq.full)
  var empty = peek(dut.io.deq.empty)

  // write into the buffer
  poke(dut.io.enq.din, 0x12)
  poke(dut.io.enq.write, 1)
  step(1)
  full = peek(dut.io.enq.full)

  poke(dut.io.enq.din, 0xff)
  poke(dut.io.enq.write, 0)
  step(1)
  full = peek(dut.io.enq.full)

  step(3) // see the bubbling of the first element

  // Fill the whole buffer with a check for full condition
  // Only every second cycle a write can happen.
  for (i <- 0 until 7) {
    full = peek(dut.io.enq.full)
    poke(dut.io.enq.din, 0x80 + i)
    if (full == 0) {
      poke(dut.io.enq.write, 1)
    } else {
      poke(dut.io.enq.write, 0)
    }
    step(1)
  }

  // Now we know it is full, so do a single read and watch
  // how this empty slot bubble up to the FIFO input.
  poke(dut.io.deq.read, 1)
  step(1)
  poke(dut.io.deq.read, 0)
  step(6)

  // New read out the whole buffer.
  // Also watch that maximum read out is every second clock cycle
  for (i <- 0 until 7) {
    empty = peek(dut.io.deq.empty)
    if (empty == 0) {
      poke(dut.io.deq.read, 1)
    } else {
      poke(dut.io.deq.read, 0)
    }
    step(1)
  }

  // Now write and read at maximum speed for some time
  for (i <- 1 until 16) {
    full = peek(dut.io.enq.full)
    poke(dut.io.enq.din, i)
    if (full == 0) {
      poke(dut.io.enq.write, 1)
    } else {
      poke(dut.io.enq.write, 0)
    }
    empty = peek(dut.io.deq.empty)
    if (empty == 0) {
      poke(dut.io.deq.read, 1)
    } else {
      poke(dut.io.deq.read, 0)
    }
    step(1)
  }

}

class BubbleFifoSpec extends FlatSpec with Matchers {

  "BubbleFifo" should "pass" in {
    chisel3.iotesters.Driver.execute(Array("--generate-vcd-output", "on"), () => new BubbleFifo(8, 4)) {
      c => new FifoTester(c)
    } should be(true)
  }
}

