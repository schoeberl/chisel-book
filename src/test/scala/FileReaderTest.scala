import chisel3._
import chiseltest._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

class FileReaderTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "FileReader"

  it should "pass" in {
    test(new FileReader) { dut =>
      val res = Array(123, 255, 0, 1, 0)
      for (i <- 0 until res.length) {
        dut.io.address.poke(i.U)
        dut.io.data.expect(res(i).U)
      }
    }
  }
}
