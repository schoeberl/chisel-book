import chisel3._
import chiseltest._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

class PwmTest extends AnyFlatSpec with ChiselScalatestTester {
  "PWM" should "pass" in {
    test(new Pwm).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(50001)
      // Just let it run to generate the waveform
      dut.clock.step(50000)
    }
  }
}
