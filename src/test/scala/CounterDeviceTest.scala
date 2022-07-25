import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class CounterDeviceTest extends AnyFlatSpec with ChiselScalatestTester {
  "CounterDevice" should "work" in {
    test(new CounterDevice()) { dut =>

      dut.clock.step()
      dut.io.ack.expect(false.B)
      dut.clock.step()
      dut.io.addr.poke(0.U)
      dut.io.rd.poke(true.B)
      dut.io.ack.expect(false.B)
      dut.clock.step()
      dut.io.rd.poke(false.B)
      dut.io.ack.expect(true.B)
      dut.clock.step(100)
      dut.io.rd.poke(true.B)
      dut.io.addr.poke(1.U)
      dut.clock.step()
      assert(dut.io.dout.peek().litValue > 100)
      dut.io.wr.poke(true.B)
      dut.io.din.poke(0.U)
      dut.clock.step()
      dut.io.wr.poke(false.B)
      dut.io.rd.poke(true.B)
      dut.clock.step()
      dut.io.dout.expect(1.U)
      dut.io.addr.poke(0.U)
      dut.clock.step()
      assert(dut.io.dout.peek().litValue > 100)
    }
  }
}