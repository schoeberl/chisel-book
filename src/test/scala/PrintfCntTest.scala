import chisel3._
import chiseltest._
import org.scalatest._

class PrintfCntTest extends FlatSpec with ChiselScalatestTester {
  "Printf" should "print" in {
    test(new PrintfCnt) { dut =>
      for (i <- 0 until 8) {
        println(s"From tester: ${dut.io.out.peek.litValue}")
        dut.clock.step()
      }
    }
  }
}
