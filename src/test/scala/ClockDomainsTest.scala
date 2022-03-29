import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class ClockDomainsTest extends AnyFlatSpec with ChiselScalatestTester {
  "Clock domains" should "pass" in {
    test(new ClockDomains).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.step(20)
    }
  }
}
