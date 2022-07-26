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

      dut.io.selMux.poke(0.U)
      dut.io.muxOut.expect(0.U)
      dut.io.selMux.poke(1.U)
      dut.io.muxOut.expect(11.U)
      dut.io.selMux.poke(2.U)
      dut.io.muxOut.expect(22.U)
    }
  }
  it should "pass VecInit" in {
    test(new BundleVec) { dut =>
      dut.io.selVecCond.poke(false.B)
      for (i <- 0 until 3) {
        dut.io.selVec.poke(i.U)
        dut.io.defVecOut.expect((i + 1).U)
      }
      dut.io.selVecCond.poke(true.B)
      for (i <- 0 until 3) {
        dut.io.selVec.poke(i.U)
        dut.io.defVecOut.expect((i + 4).U)
      }

      for (i <- 0 until 3) {
        dut.io.defVecSigIn(i).poke((i * 10).U)
      }
      for (i <- 0 until 3) {
        dut.io.selVec.poke(i.U)
        dut.io.defVecSigOut.expect((i*10).U)
      }

      for (i <- 0 until 3) {
        dut.io.defVecSigIn(i).poke((i * 2).U)
        dut.io.regVecOut(i).expect(i.U)
      }
      dut.clock.step()
      for (i <- 0 until 3) {
        dut.io.regVecOut(i).expect((i * 2).U)
      }

      for (i <- 0 until 32) {
        dut.io.selVec.poke(i.U)
        dut.io.resetRegFileOut.expect(0.U)
      }
    }
  }
}
