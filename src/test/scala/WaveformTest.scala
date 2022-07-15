import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

//- start test_bench_wave
class WaveformTest extends AnyFlatSpec with ChiselScalatestTester {
  "Waveform" should "pass" in {
    test(new DeviceUnderTest)
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.io.a.poke(0.U)
      dut.io.b.poke(0.U)
      dut.clock.step()
      dut.io.a.poke(1.U)
      dut.io.b.poke(0.U)
      dut.clock.step()
      dut.io.a.poke(0.U)
      dut.io.b.poke(1.U)
      dut.clock.step()
      dut.io.a.poke(1.U)
      dut.io.b.poke(1.U)
      dut.clock.step()
    }
  }
}
//- end

//- start test_bench_wavecnt
class WaveformCounterTest extends AnyFlatSpec with ChiselScalatestTester {
  "WaveformCounter" should "pass" in {
    test(new DeviceUnderTest)
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      for (a <- 0 until 4) {
        for (b <- 0 until 4) {
          dut.io.a.poke(a.U)
          dut.io.b.poke(b.U)
          dut.clock.step()
        }
      }
    }
  }
}
//- end
