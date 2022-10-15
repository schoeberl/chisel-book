import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class InterconnectTest extends AnyFlatSpec with ChiselScalatestTester {
  "Interconnect" should "should work" in {
    test(new UseMemMappedRV(UInt(16.W))) { dut =>

      def step(n: Int = 1) = {
        dut.clock.step(n)
      }
      def read(addr: Int) = {
        dut.io.mem.address.poke(addr.U)
        dut.io.mem.rd.poke(true.B)
        step()
        dut.io.mem.rd.poke(false.B)
        while (!dut.io.mem.ack.peek().litToBoolean) {
          step()
        }
        dut.io.mem.rdData.peek().litValue
      }
      def write(addr: Int, data: Int) = {
        dut.io.mem.address.poke(addr.U)
        dut.io.mem.wrData.poke(data.U)
        dut.io.mem.wr.poke(true.B)
        step()
        dut.io.mem.wr.poke(false.B)
        while (!dut.io.mem.ack.peek().litToBoolean) {
          step()
        }
      }

      step(5)
      assert(read(0) == 1, "TX flag should be set")
      write(1, 123)
      step(10)
      assert(read(0) == 3, "TX and RX flag should be set")
      assert(read(1) == 123, "RX value should be correct")
    }
  }

}
