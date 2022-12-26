import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class CounterDeviceTest extends AnyFlatSpec with ChiselScalatestTester {
  //- start test_counter
  "CounterDevice" should "work" in {
    test(new CounterDevice()) { dut =>
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
      assert(dut.io.rdData.peekInt() > 100)
      dut.io.wr.poke(true.B)
      dut.io.wrData.poke(0.U)
      dut.clock.step()
      dut.io.wr.poke(false.B)
      dut.io.rd.poke(true.B)
      dut.clock.step()
      dut.io.rdData.expect(1.U)
      dut.io.addr.poke(0.U)
      dut.clock.step()
      assert(dut.io.rdData.peekInt() > 100)
    }
  }
  //- end

  //- start test_counter_fun
  "CounterDevice" should "work with functions" in {
    test(new CounterDevice()) { dut =>

      def step(n: Int = 1) = {
        dut.clock.step(n)
      }
      def read(addr: Int) = {
        dut.io.addr.poke(addr.U)
        dut.io.rd.poke(true.B)
        step()
        dut.io.rd.poke(false.B)
        while (!dut.io.ack.peekBoolean()) {
          step()
        }
        dut.io.rdData.peekInt()
      }
      def write(addr: Int, data: Int) = {
        dut.io.addr.poke(addr.U)
        dut.io.wrData.poke(data.U)
        dut.io.wr.poke(true.B)
        step()
        dut.io.wr.poke(false.B)
        while (!dut.io.ack.peekBoolean()) {
          step()
        }
      }

      for (i <- 0 until 4) {
        assert(read(i) < 10, s"Counter $i should have just started")
      }
      step(100)
      for (i <- 0 until 4) {
        assert(read(i) > 100, s"Counter $i should advance")
      }
      write(2, 0)
      write(3, 1000)
      assert(read(2) < 5, "Counter should reset")
      assert(read(3) > 1000, "Counter should load")
    }
  }
  //- end

}
