import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class SyncResetTest extends AnyFlatSpec with ChiselScalatestTester {
  "SyncReset" should "synchronize" in {
    test(new SyncReset).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      dut.clock.step(20)
    }
  }

}
