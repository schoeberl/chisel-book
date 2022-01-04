import chisel3._
import chiseltest._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

class DrawingTest extends AnyFlatSpec with ChiselScalatestTester {
  "DrawAcc" should "pass" in {
    test(new DrawAcc) { dut =>
      dut.io.din.poke(3.U)
      dut.io.sel.poke(0.U)
      dut.clock.step()
      dut.io.dout.expect(0.U)
      dut.io.sel.poke(2.U)
      dut.clock.step()
      dut.io.dout.expect(3.U)
      dut.clock.step()
      dut.io.dout.expect(6.U)
      dut.io.sel.poke(1.U)
      dut.clock.step()
      dut.io.dout.expect(0.U)
    }
  }

  "DrawMux6" should "pass" in {
    test(new DrawMux6) { dut =>
      dut.io.sel.poke(0.U)
      dut.clock.step()
      dut.io.dout.expect(0.U)
      dut.io.sel.poke(1.U)
      dut.clock.step()
      dut.io.dout.expect(11.U)
      dut.io.sel.poke(5.U)
      dut.clock.step()
      dut.io.dout.expect(55.U)
    }
  }
}
