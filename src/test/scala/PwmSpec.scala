import chisel3.iotesters.PeekPokeTester
import org.scalatest._

class PwmSpec extends FlatSpec with Matchers {

  "PWm" should "pass" in {
    chisel3.iotesters.Driver.execute(Array("--generate-vcd-output", "on"), () => new Pwm()) { c =>
      new PeekPokeTester(c) {

        // just let it run to generate the waveform
        step(50000)
        // peek(c.io.led(0))
      }
    } should be (true)
  }

}

