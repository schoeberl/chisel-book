import chisel3._
import chiseltest._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

class MultiClockMemoryTester extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "Multi-clock memory"

  it should "operate" in {
    test(new MultiClockMemory(3)) { dut =>
      fork {
        // Port 1
        val p1 = dut.io.ps(0)
        p1.clk.poke(false.B)
        p1.addr.poke(0.U)
        p1.datai.poke(42.U)
        p1.en.poke(true.B)
        p1.we.poke(true.B)
        p1.clk.poke(true.B)
        p1.clk.poke(false.B)
        dut.clock.step()
        p1.we.poke(false.B)
        p1.addr.poke(1023.U)
        p1.clk.poke(true.B)
        p1.datao.expect(13.U)
      }.fork {
        // Port 2
        val p2 = dut.io.ps(1)
        p2.clk.poke(false.B)
        p2.addr.poke(13.U)
        p2.datai.poke(1337.U)
        p2.en.poke(true.B)
        p2.we.poke(true.B)
        p2.clk.poke(true.B)
        p2.clk.poke(false.B)
        dut.clock.step()
        p2.we.poke(false.B)
        p2.addr.poke(13.U)
        p2.clk.poke(true.B)
        p2.datao.expect(1337.U)
      }.fork {
        // Port 3
        val p3 = dut.io.ps(2)
        p3.clk.poke(false.B)
        p3.addr.poke(1023.U)
        p3.datai.poke(13.U)
        p3.en.poke(true.B)
        p3.we.poke(true.B)
        p3.clk.poke(true.B)
        p3.clk.poke(false.B)
        dut.clock.step()
        p3.we.poke(false.B)
        p3.addr.poke(0.U)
        p3.clk.poke(true.B)
        p3.datao.expect(42.U)
      }.join()
    }
  }
}
