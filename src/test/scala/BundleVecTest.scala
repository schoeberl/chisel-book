import chisel3._
import chiseltest._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

class BundleVecTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "BundleVec"

  it should "pass" in {
    test(new BundleVec) { dut =>
      dut.io.chan.data.expect(123.U)
      dut.io.chan.valid.expect(true.B)
      dut.io.chan2.data.expect(123.U)

      dut.io.idx.poke(0.U)
      dut.io.array.expect(1.U)
      dut.io.idx.poke(1.U)
      dut.io.array.expect(3.U)
      dut.io.idx.poke(2.U)
      dut.io.array.expect(5.U)

      dut.io.bvOut.expect(13.U)

      dut.io.chreg.valid.expect(false.B)
      dut.io.chreg.data.expect(0.U)
      dut.io.idx.poke(2.U)
      dut.io.dIn.poke(5.U)
      dut.clock.step()
      dut.io.chreg.valid.expect(true.B)
      dut.io.chreg.data.expect(1.U)
      dut.io.idx.poke(2.U)
      dut.io.dIn.poke(7.U)
      dut.io.dOut.expect(5.U)
      dut.clock.step()
      dut.io.dOut.expect(7.U)

      dut.io.wrIdx.poke(1.U)
      dut.io.din.poke(3.U)
      dut.clock.step()
      dut.io.wrIdx.poke(2.U)
      dut.io.din.poke(7.U)
      dut.clock.step()
      dut.io.rdIdx.poke(1.U)
      dut.io.dout.expect(3.U)
      dut.io.rdIdx.poke(2.U)
      dut.io.dout.expect(7.U)
    }
  }
}
