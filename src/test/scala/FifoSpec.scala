package fifo

import chisel3._
import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class FifoTester[T <: Fifo[_ <: Data]](dut: T) extends PeekPokeTester(dut) {
// class FifoTester[D <: Data, T[_ <: Data] <: Fifo[_]](dut: T[D]) extends PeekPokeTester(dut) {
// class FifoTester(dut: BubbleFifo[UInt]) extends PeekPokeTester(dut) {

  // some defaults for all signals
  poke(dut.io.enq.bits.asUInt(), 0xab)
  poke(dut.io.enq.valid, 0)
  poke(dut.io.deq.ready, 0)
  step(1)
  var ready = peek(dut.io.enq.ready)
  var valid = peek(dut.io.deq.valid)
  // write one value and expect it on the deq side
  poke(dut.io.enq.bits.asUInt(), 0x123)
  poke(dut.io.enq.valid, 1)
  step(1)
  poke(dut.io.enq.bits.asUInt(), 0xab)
  poke(dut.io.enq.valid, 0)
  step(12)
  expect(dut.io.enq.ready, 1)
  expect(dut.io.deq.valid, 1)
  expect(dut.io.deq.bits.asUInt(), 0x123)
  // read it out
  poke(dut.io.deq.ready, 1)
  step(1)
  expect(dut.io.deq.valid, 0)
  poke(dut.io.deq.ready, 0)
  step(1)
  // file the whole buffer
  // TODO: how do I access the parameter depth from the dut?
  var cnt = 1
  // we are always valid
  poke(dut.io.enq.valid, 1)
  for (i <- 0 until 12) {
    poke(dut.io.enq.bits.asUInt(), cnt.U)
    if (peek(dut.io.enq.ready) != 0) {
      cnt += 1
    }
    step(1)
  }
  println(s"Wrote ${cnt-1} words")
  expect(dut.io.enq.ready, 0)
  expect(dut.io.deq.valid, 1)
  expect(dut.io.deq.bits.asUInt(), 1)

  // now read it back
  var expected = 1
  // no new input
  poke(dut.io.enq.valid, 0)
  // always ready on the reader side
  poke(dut.io.deq.ready, 1)
  for (i <- 0 until 12) {
    if (peek(dut.io.deq.valid) != 0) {
      expect(dut.io.deq.bits.asUInt(), expected)
      expected += 1
    }
    step(1)
  }

  // now do the speed test
  poke(dut.io.enq.valid, 1)
  poke(dut.io.deq.ready, 1)
  cnt = 0
  for (i <- 0 until 100) {
    poke(dut.io.enq.bits.asUInt(), i.U)
    if (peek(dut.io.enq.ready) != 0) {
      cnt += 1
    }
    step(1)
  }
  val cycles = 100.0 / cnt
  println(s"$cnt words in 100 clock cycles, $cycles clock cycles per word")
  assert(cycles >= 0.99, "Cannot be faster than one clock cycle per word")
}

class FifoSpec extends FlatSpec with Matchers {

  "BubbleFifo" should "pass" in {
    chisel3.iotesters.Driver.execute(Array("--target-dir", "generated", "--generate-vcd-output", "on"),
      () => new BubbleFifo(UInt(16.W), 4)) { c =>
      new FifoTester(c)
    } should be (true)
  }

  "DoubleBufferFifo" should "pass" in {
    chisel3.iotesters.Driver.execute(Array("--target-dir", "generated", "--generate-vcd-output", "on"),
      () => new DoubleBufferFifo(UInt(16.W), 4)) { c =>
      new FifoTester(c)
    } should be (true)
  }
}

