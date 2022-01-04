//- start test_import
import chisel3._
import chiseltest._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec
//- end

//- start test_bench_simple
class SimpleTest extends AnyFlatSpec with ChiselScalatestTester {
  "DUT" should "pass" in {
    test(new DeviceUnderTest) { dut =>
      dut.io.a.poke(0.U)
      dut.io.b.poke(1.U)
      dut.clock.step()
      println("Result is: " + dut.io.out.peek().toString)
      dut.io.a.poke(3.U)
      dut.io.b.poke(2.U)
      dut.clock.step()
      println("Result is: " + dut.io.out.peek().toString)
    }
  }
}
//- end

//- start test_bench
class SimpleTestExpect extends AnyFlatSpec with ChiselScalatestTester {
  "DUT" should "pass" in {
    test(new DeviceUnderTest) { dut =>
      dut.io.a.poke(0.U)
      dut.io.b.poke(1.U)
      dut.clock.step()
      dut.io.out.expect(0.U)
      dut.io.a.poke(3.U)
      dut.io.b.poke(2.U)
      dut.clock.step()
      dut.io.out.expect(2.U)
    }
  }
}
//- end

class SimplePrintfTest extends AnyFlatSpec with ChiselScalatestTester {
  "WaveformCounter" should "pass" in {
    test(new DeviceUnderTestPrintf) { dut =>
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
