import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class ConvertTest extends AnyFlatSpec with ChiselScalatestTester {
  "Convert" should "work" in {
    test(new Convert) { dut =>
      dut.io.vec(0).poke(0x12.U)
      dut.io.vec(1).poke(0.U)
      dut.io.vec(2).poke(0xcd.U)
      dut.io.vec(3).poke(0xab.U)

      dut.io.int32.expect(BigInt(0xabcd0012L))

      dut.io.in.poke(0x56.U)
      dut.io.in2.poke(0x1234.U)
      dut.clock.step()
      dut.io.out.expect(0x561234)
    }
  }
}
