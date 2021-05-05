import chisel3._
import chiseltest._
import chiseltest.experimental.TestOptionBuilder._
import chiseltest.internal.WriteVcdAnnotation
import org.scalatest._

class PwmTest extends FlatSpec with ChiselScalatestTester {
  "PWM" should "pass" in {
    test(new Pwm).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.setTimeout(50001)
      // Just let it run to generate the waveform
      dut.clock.step(50000)
    }
  }
}
