
//- start test_import_wave
import chisel3.iotesters.PeekPokeTester
import chisel3.iotesters.Driver
import org.scalatest._
//- end

//- start test_bench_wave
class WaveformTester(dut: DeviceUnderTest) extends PeekPokeTester(dut) {

  poke(dut.io.a, 0)
  poke(dut.io.b, 0)
  step(1)
  poke(dut.io.a, 1)
  poke(dut.io.b, 0)
  step(1)
  poke(dut.io.a, 0)
  poke(dut.io.b, 1)
  step(1)
  poke(dut.io.a, 1)
  poke(dut.io.b, 1)
  step(1)
}
//- end

//- start test_bench_wave_cnt
class WaveformCounterTester(dut: DeviceUnderTest) extends PeekPokeTester(dut) {

  for (a <- 0 until 4) {
    for (b <- 0 until 4) {
      poke(dut.io.a, a)
      poke(dut.io.b, b)
      step(1)
    }
  }
}
//- end

//- start scalatest_wave
class WaveformSpec extends FlatSpec with Matchers {
  "Waveform" should "pass" in {
    Driver.execute(Array("--generate-vcd-output", "on"), () => new DeviceUnderTest()) { c =>
      new WaveformTester(c)
    } should be (true)
  }
}
//- end

//- start scalatest_wave_cnt
class WaveformCounterSpec extends FlatSpec with Matchers {

  "WaveformCounter" should "pass" in {
    Driver.execute(Array("--generate-vcd-output", "on"), () => new DeviceUnderTest()) { c =>
      new WaveformCounterTester(c)
    } should be (true)
  }
}
//- end
