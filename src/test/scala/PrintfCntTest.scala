import chisel3._
import chiseltest._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

class PrintfCntTest extends AnyFlatSpec with ChiselScalatestTester {
  "Printf" should "print" in {
    test(new PrintfCnt) { dut =>
      for (i <- 0 until 8) {
        println(s"From tester: ${dut.io.out.peekInt()}")
        dut.clock.step()
      }
    }
  }
}
