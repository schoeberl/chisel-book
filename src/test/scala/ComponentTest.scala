import chisel3._
import chiseltest._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

class ComponentTest extends AnyFlatSpec with ChiselScalatestTester {
  "ComponentFn" should "pass" in {
    test(new CompFn) { dut =>
      dut.io.a.poke(0x1.U)
      dut.io.b.poke(0x2.U)
      dut.io.out.expect(0x3.U)
      dut.io.c.poke(5.U)
      dut.io.d.poke(6.U)
      dut.io.out2.expect((11-1).U)

      dut.io.del.poke(5.U)
      dut.clock.step(5)
      dut.io.out3.expect(5.U)
      dut.io.del.poke(7.U)
      dut.clock.step()
      dut.io.out3.expect(5.U)
      dut.clock.step()
      dut.io.out3.expect(7.U)
    }
  }

  "Count10" should "count correctly" in {
    test(new Count10) { dut =>
      for (i <- 0 until 10) {
        dut.io.dout.expect(i.U)
        dut.clock.step()
      }
      dut.io.dout.expect(0.U)
    }
  }
}
