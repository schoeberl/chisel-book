import chisel3._
import chiseltest._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

class GenHardwareTest extends AnyFlatSpec with ChiselScalatestTester {
  "GenHardware" should "work" in {
    test(new GenHardware) { dut =>
      dut.io.data(0).expect('H'.U)
      dut.io.data(11).expect('!'.U)
      dut.io.len.expect(12.U)
      for (i <- 0 until 6) {
        dut.io.squareIn.poke(i.U)
        dut.io.squareOut.expect((i * i).U)
      }
    }
  }
}
