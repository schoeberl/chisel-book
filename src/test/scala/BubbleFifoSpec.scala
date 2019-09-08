import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class FifoTester(dut: BubbleFifo) extends PeekPokeTester(dut) {

  // some defaults for all signals
  // 所有信号的一些默认值
  poke(dut.io.enq.din, 0xab)
  poke(dut.io.enq.write, 0)
  poke(dut.io.deq.read, 0)
  step(1)
  var full = peek(dut.io.enq.full)
  var empty = peek(dut.io.deq.empty)

  // write into the buffer
  // 写入到缓存
  poke(dut.io.enq.din, 0x12)
  poke(dut.io.enq.write, 1)
  step(1)
  full = peek(dut.io.enq.full)

  poke(dut.io.enq.din, 0xff)
  poke(dut.io.enq.write, 0)
  step(1)
  full = peek(dut.io.enq.full)

  // see the bubbling of the first element
  // 查看第一个元素的冒泡
  step(3) 

  // Fill the whole buffer with a check for full condition
  // Only every second cycle a write can happen.
  // 填充整个缓存查看整体情况
  // 只能发生在每周期写入一个的情况
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
  // 现在我们知道它是满了，所以做一个简单的写入和观察
  // 看一下
  poke(dut.io.deq.read, 1)
  step(1)
  poke(dut.io.deq.read, 0)
  step(6)

  // New read out the whole buffer.
  // Also watch that maximum read out is every second clock cycle
  // 现在读出缓存
  // 并且观察
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
  // 现在以最快速度写入和读取
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
    chisel3.iotesters.Driver.execute(Array("--target-dir", "generated", "--fint-write-vcd"), () => new BubbleFifo(8, 4)) {
      // iotesters.Driver.execute(Array("--target-dir", "generated", "--fint-write-vcd", "--wave-form-file-name", "generated/BubbleFifo.vcd"), () => new BubbleFifo(8, 4)) {
      c => new FifoTester(c)
    } should be(true)
  }
}

