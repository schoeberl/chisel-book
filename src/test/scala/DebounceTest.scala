import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class DebounceTest extends AnyFlatSpec with ChiselScalatestTester {
  val FAC = 100

  "Debounce" should "debounce inputs" in {
    test(new Debounce(FAC)).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.io.btnU.poke(false.B)
      dut.clock.step(3)
      dut.io.led.expect(0.U)
      dut.clock.step(FAC/3)
      dut.io.btnU.poke(true.B)
      dut.clock.step(FAC/30)
      dut.io.btnU.poke(false.B)
      dut.clock.step(FAC/30)
      dut.io.btnU.poke(true.B)
      dut.clock.step(FAC)
      dut.io.led.expect(0.U)
      dut.clock.step(FAC)
      dut.io.led.expect(0.U)
      dut.clock.step(FAC)
      dut.io.led.expect(1.U)
    }
  }

  "DebounceFunc" should "debounce" in {
    test(new DebounceFunc(FAC)).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.io.btnU.poke(false.B)
      dut.clock.step(3)
      dut.io.led.expect(false.B)
      dut.clock.step(FAC/3)
      dut.io.btnU.poke(true.B)
      dut.clock.step(FAC/30)
      dut.io.btnU.poke(false.B)
      dut.clock.step(FAC/30)
      dut.io.btnU.poke(true.B)
      dut.clock.step(FAC)
      dut.io.led.expect(false.B)
      dut.clock.step(FAC)
      dut.io.led.expect(false.B)
      dut.clock.step(FAC)
      dut.io.led.expect(1.U)
    }
  }
}