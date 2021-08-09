import chisel3._
import chiseltest._
import org.scalatest._

class LogicTest extends FlatSpec with ChiselScalatestTester {
  "Logic" should "pass" in {
    test(new Logic) { dut =>
      dut.io.a.poke(1.U)
      dut.io.b.poke(0.U)
      dut.io.c.poke(1.U)
      dut.clock.step()
      dut.io.out.expect(1.U)
      dut.io.cat.expect("hff01".U)
      dut.io.ch.expect(65.U)
    }
  }
}
