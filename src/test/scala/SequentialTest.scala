import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class SequentialTest extends AnyFlatSpec with ChiselScalatestTester {
  "Sequential" should "pass" in {
    test(new Sequential) { dut =>
      dut.io.d.poke(0xc.U)
      dut.io.d2.poke(0xa.U)
      dut.io.d3.poke(0xf.U)
      dut.io.ena.poke(true.B)
      dut.io.q3.expect(0x0.U)
      dut.io.q5.expect(0x0.U)
      dut.io.q7.expect(0x0.U)
      dut.io.riseIn.poke(false.B)
      dut.clock.step()
      dut.io.d.poke(0x1.U)
      dut.io.d2.poke(0x2.U)
      dut.io.d3.poke(0x3.U)
      dut.io.ena.poke(false.B)
      dut.io.q.expect(0xc.U)
      dut.io.q2.expect(0xa.U)
      dut.io.q3.expect(0xf.U)
      dut.io.q4.expect(0xf.U)
      dut.io.q5.expect(0xf.U)
      dut.io.q6.expect(0xf.U)
      dut.io.q7.expect(0xf.U)
      dut.io.riseIn.poke(true.B)
      dut.io.riseOut.expect(true.B)
      dut.clock.step()
      dut.io.q.expect(0x1.U)
      dut.io.q2.expect(0x2.U)
      dut.io.q3.expect(0x3.U)
      dut.io.q4.expect(0xf.U)
      dut.io.q5.expect(0xf.U)
      dut.io.q6.expect(0xf.U)
      dut.io.q7.expect(0xf.U)
      dut.io.ena.poke(true.B)
      dut.io.riseOut.expect(false.B)
      dut.clock.step()
      dut.io.q4.expect(0x3.U)
      dut.io.q5.expect(0x3.U)
      dut.io.q6.expect(0x3.U)
      dut.io.q7.expect(0x3.U)
      dut.io.riseOut.expect(false.B)
    }
  }

  "Counter" should "pass" in {
    test(new SequCounter) { dut =>
      dut.io.out.expect(0.U)
      dut.io.eventCnt.expect(0.U)
      dut.io.event.poke(false.B)
      dut.clock.step()
      dut.io.out.expect(1.U)
      dut.io.eventCnt.expect(0.U)
      dut.io.event.poke(true.B)
      dut.clock.step()
      dut.io.out.expect(2.U)
      dut.io.eventCnt.expect(1.U)
      dut.io.event.poke(false.B)
      dut.clock.step(15)
      dut.io.out.expect(1.U)
      dut.io.eventCnt.expect(1.U)
    }
  }

  "Tick" should "pass" in {
    test(new SequCounter) { dut =>
      dut.io.tick.expect(false.B)
      dut.io.lowCnt.expect(0.U)
      dut.clock.step()
      dut.io.tick.expect(false.B)
      dut.clock.step()
      dut.io.tick.expect(false.B)
      dut.clock.step()
      dut.io.tick.expect(false.B)
      dut.io.lowCnt.expect(0.U)
      dut.clock.step()
      dut.io.tick.expect(true.B)
      dut.io.lowCnt.expect(0.U)
      dut.clock.step()
      dut.io.tick.expect(false.B)
      dut.io.lowCnt.expect(1.U)
      dut.clock.step(4)
      dut.io.tick.expect(true.B)
      dut.io.lowCnt.expect(1.U)
      dut.clock.step()
      dut.io.tick.expect(false.B)
      dut.io.lowCnt.expect(2.U)
    }
  }
}
