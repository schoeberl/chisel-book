import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class ComparatorTest extends AnyFlatSpec with ChiselScalatestTester {
  "Comparator" should "pass" in {
    test(new Comparator()) { dut =>
      dut.io.a.poke(123.U)
      dut.io.b.poke(123.U)
      dut.clock.step()
      dut.io.equ.expect(true.B)
      dut.io.gt.expect(false.B)
      dut.io.a.poke(234.U)
      dut.io.b.poke(123.U)
      dut.clock.step()
      dut.io.equ.expect(false.B)
      dut.io.gt.expect(true.B)
      dut.io.a.poke(1.U)
      dut.io.b.poke(2.U)
      dut.clock.step()
      dut.io.equ.expect(false.B)
      dut.io.gt.expect(false.B)
    }
  }
}
