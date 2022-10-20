import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class FsmTest extends AnyFlatSpec with ChiselScalatestTester {
  "SimpleFsm" should "pass" in {
    test(new SimpleFsm) { dut =>
      dut.io.clear.poke(false.B)
      dut.io.badEvent.poke(false.B)
      dut.io.ringBell.expect(false.B)
      dut.clock.step()
      dut.io.ringBell.expect(false.B)
      dut.io.badEvent.poke(true.B)
      dut.clock.step()
      dut.io.ringBell.expect(false.B)
      dut.io.badEvent.poke(false.B)
      dut.clock.step()
      dut.io.ringBell.expect(false.B)
      dut.io.badEvent.poke(true.B)
      dut.clock.step()
      dut.io.ringBell.expect(true.B)
      dut.io.badEvent.poke(false.B)
      dut.clock.step()
      dut.io.ringBell.expect(true.B)
      dut.io.clear.poke(true.B)
      dut.clock.step()
      dut.io.ringBell.expect(false.B)
    }
  }

  "RisingFsm" should "pass" in {
    test(new RisingFsm) { dut =>
      dut.io.din.poke(false.B)
      dut.io.risingEdge.expect(false.B)
      dut.clock.step()
      dut.io.risingEdge.expect(false.B)
      dut.clock.step()
      dut.io.din.poke(true.B)
      dut.io.risingEdge.expect(true.B)
      dut.clock.step()
      dut.io.risingEdge.expect(false.B)
      dut.clock.step()
      dut.io.risingEdge.expect(false.B)
      dut.io.din.poke(false.B)
      dut.io.risingEdge.expect(false.B)
      dut.clock.step()
      dut.io.risingEdge.expect(false.B)
    }
  }

  "RisingMooreFsm" should "pass" in {
    test(new RisingMooreFsm) { dut =>
      dut.io.din.poke(false.B)
      dut.io.risingEdge.expect(false.B)
      dut.clock.step()
      dut.io.risingEdge.expect(false.B)
      dut.clock.step()
      dut.io.din.poke(true.B)
      dut.io.risingEdge.expect(false.B)
      dut.clock.step()
      dut.io.risingEdge.expect(true.B)
      dut.clock.step()
      dut.io.risingEdge.expect(false.B)
      dut.io.din.poke(false.B)
      dut.io.risingEdge.expect(false.B)
      dut.clock.step()
      dut.io.risingEdge.expect(false.B)
    }
  }
}
