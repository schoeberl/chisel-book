import chisel3._
import chiseltest._
import org.scalatest._

class GenHardwareTest extends FlatSpec with ChiselScalatestTester {
  "GenHardware" should "work" in {
    test(new GenHardware) { dut =>
      dut.io.data(0).expect('H'.U)
      dut.io.data(11).expect('!'.U)
      dut.io.len.expect(12.U)
    }
  }
}
