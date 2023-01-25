import chisel3._
import chiseltest._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

class BubbleFifoTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "Bubble FIFO"

  it should "pass" in {
    test(new BubbleFifo(8, 4)).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      // Some default signal values.
      dut.io.enq.din.poke("hab".U)
      dut.io.enq.write.poke(false.B)
      dut.io.deq.read.poke(false.B)
      dut.clock.step()
      var full = dut.io.enq.full.peekBoolean()
      var empty = dut.io.deq.empty.peekBoolean()

      // Write into the buffer
      dut.io.enq.din.poke("h12".U)
      dut.io.enq.write.poke(true.B)
      dut.clock.step()
      full = dut.io.enq.full.peekBoolean()

      dut.io.enq.din.poke("hff".U)
      dut.io.enq.write.poke(false.B)
      dut.clock.step()
      full = dut.io.enq.full.peekBoolean()

      dut.clock.step() // see the bubbling of the first element

      // Fill the whole buffer with a check for full condition
      // Only every second cycle a write can happen
      for (i <- 0 until 7) {
        full = dut.io.enq.full.peekBoolean()
        dut.io.enq.din.poke((0x80 + i).U)
        dut.io.enq.write.poke((!full).B)
        dut.clock.step()
      }

      // Now we know it is full, so do a single read and watch
      // how this empty slot bubbles up to the FIFO input.
      dut.io.deq.read.poke(true.B)
      dut.io.deq.dout.expect("h12".U)
      dut.clock.step()
      dut.io.deq.read.poke(false.B)
      dut.clock.step(6)

      // Now read out the whole buffer.
      // Also watch that maximum read out is every second clock cycle.
      for (i <- 0 until 7) {
        empty = dut.io.deq.empty.peekBoolean()
        dut.io.deq.read.poke((!empty).B)
        dut.clock.step()
      }

      // Now write and read at maximum speed for some time.
      for (i <- 1 until 16) {
        full = dut.io.enq.full.peekBoolean()
        dut.io.enq.din.poke(i.U)
        dut.io.enq.write.poke((!full).B)

        empty = dut.io.deq.empty.peekBoolean()
        dut.io.deq.read.poke((!empty).B)
        dut.clock.step()
      }
    }
  }
  
//- start fork_join_example
  it should "work with multiple threads" in {
    test(new BubbleFifo(8, 4)) { dut =>
      val enq = fork {
        while (dut.io.enq.full.peekBoolean())
          dut.clock.step()
        dut.io.enq.din.poke(42.U)
        dut.io.enq.write.poke(true.B)
        dut.clock.step()
        dut.io.enq.write.poke(false.B)
      }
      while (dut.io.deq.empty.peekBoolean())
        dut.clock.step()
      dut.io.deq.dout.expect(42.U)
      dut.io.deq.read.poke(true.B)
      dut.clock.step()
      dut.io.deq.empty.expect(true.B)
      enq.join()
    }
  }
//- end
}
