import chisel3._
import chiseltest._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

class ShiftRegisterTest extends AnyFlatSpec with ChiselScalatestTester {
  "ShiftRegister" should "pass" in {
    test(new ShiftRegister) { dut =>
      dut.io.din.poke(1.U)
      dut.clock.step()
      dut.io.din.poke(0.U)
      dut.clock.step(3)
      dut.io.dout.expect(1.U)
      dut.clock.step()
      dut.io.dout.expect(0.U)
      dut.clock.step()
      dut.io.dout.expect(0.U)
    }
  }

  "ShiftRegisterParaOut" should "pass" in {
    test(new ShiftRegister) { dut =>
      dut.io.serIn.poke(1.U)
      dut.clock.step()
      dut.io.serIn.poke(0.U)
      dut.clock.step(2)
      dut.io.serIn.poke(1.U)
      dut.clock.step()
      dut.io.paraOut.expect(0x9.U)
    }
  }

  "ShiftRegisterParaLoad" should "pass" in {
    test(new ShiftRegister) { dut =>
      dut.io.d.poke(0.U)
      dut.io.load.poke(false.B)
      dut.clock.step()
      dut.io.d.poke(0xa.U)
      dut.io.load.poke(true.B)
      dut.clock.step()
      dut.io.d.poke(0.U)
      dut.io.load.poke(false.B)
      dut.io.serOut.expect(0.U)
      dut.clock.step()
      dut.io.serOut.expect(1.U)
      dut.clock.step()
      dut.io.serOut.expect(0.U)
      dut.clock.step()
      dut.io.serOut.expect(1.U)
    }
  }
}
