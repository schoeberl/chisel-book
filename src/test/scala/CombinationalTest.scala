import chisel3._
import chiseltest._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

class CombinationalTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "Combinational logic"

  it should "work with purely combinational logic" in {
    test(new Combinational) { dut =>
      dut.io.a.poke(0xc.U)
      dut.io.b.poke(0xa.U)
      dut.io.c.poke(0x1.U)
      dut.io.out.expect(0x9.U)
      dut.io.out2.expect(0x6.U)

      dut.io.a.poke(0x3.U)
      dut.io.b.poke(0x2.U)
      dut.io.c.poke(0xc.U)
      dut.io.out.expect(0xe.U)
      dut.io.out2.expect(0x1.U)
    }
  }

  it should "work with a when statement" in {
    test(new CombWhen) { dut =>
      dut.io.cond.poke(false.B)
      dut.io.out.expect(0.U)
      dut.io.cond.poke(true.B)
      dut.io.out.expect(3.U)
    }
  }

  it should "work with a when-otherwise statement" in {
    test(new CombOther) { dut =>
      dut.io.cond.poke(false.B)
      dut.io.out.expect(2.U)
      dut.io.cond.poke(true.B)
      dut.io.out.expect(1.U)
    }
  }

  it should "work with a when-elsewhen-otherwise statement" in {
    test(new CombElseWhen) { dut =>
      dut.io.cond.poke(false.B)
      dut.io.cond2.poke(false.B)
      dut.io.out.expect(3.U)
      dut.io.cond.poke(true.B)
      dut.io.out.expect(1.U)
      dut.io.cond.poke(false.B)
      dut.io.cond2.poke(true.B)
      dut.io.out.expect(2.U)
      dut.io.cond.poke(true.B)
      dut.io.out.expect(1.U)
    }
  }

  it should "work with a default value" in {
    test(new CombWireDefault) { dut =>
      dut.io.cond.poke(false.B)
      dut.io.out.expect(0.U)
      dut.io.cond.poke(true.B)
      dut.io.out.expect(3.U)
    }
  }
}
