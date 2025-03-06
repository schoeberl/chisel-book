package verilog

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class BasicTest extends AnyFlatSpec with ChiselScalatestTester {
  "Adder" should "pass" in {
    test(new AdderTop()).withAnnotations(Seq(VerilatorBackendAnnotation)) { dut =>
      dut.io.a.poke(1.U)
      dut.io.b.poke(3.U)
      dut.clock.step()
      dut.io.sum.expect(4.U)
      dut.io.c.poke(1.U)
      dut.io.d.poke(3.U)
      dut.clock.step()
      dut.io.s.expect(4.U)
    }
  }
  /*
    "Use Adder" should "pass" in {
      test(new AdderTop()).withAnnotations(Seq(VerilatorBackendAnnotation)) { dut =>
        dut.io.a.poke(1.U)
        dut.io.b.poke(3.U)
        dut.clock.step()
        dut.io.sum.expect(4.U)
        dut.io.c.poke(1.U)
        dut.io.d.poke(3.U)
        dut.clock.step()
        dut.io.s.expect(4.U)
      }
    }

   */

  "Register" should "pass" in {
    test(new RegisterTop()).withAnnotations(Seq(VerilatorBackendAnnotation)) { dut =>
      dut.io.en.poke(1.U)
      dut.io.in.poke(1.U)
      dut.io.out.expect(0.U)
      dut.io.out2.expect(0.U)
      dut.clock.step()
      dut.io.out.expect(1.U)
      dut.io.out2.expect(1.U)
      dut.io.in.poke(123.U)
      dut.io.out.expect(1.U)
      dut.io.out2.expect(1.U)
      dut.clock.step()
      dut.io.out.expect(123.U)
      dut.io.out2.expect(123.U)
      dut.io.in.poke(45.U)
      dut.io.en.poke(0.U)
      dut.io.out.expect(123.U)
      dut.io.out2.expect(123.U)
      dut.clock.step()
      dut.io.out.expect(123.U)
      dut.io.out2.expect(123.U)
    }
  }
  "Comb" should "pass" in {
    test(new CombTop()).withAnnotations(Seq(VerilatorBackendAnnotation)) { dut =>
      dut.io.in.poke(1.U)
      dut.io.sel.poke(0.U)
      dut.clock.step()
      dut.io.out.expect(1.U)
      dut.io.out2.expect(1.U)
      dut.io.sel.poke(1.U)
      dut.clock.step()
      dut.io.out.expect(0.U)
      dut.io.out2.expect(0.U)
      dut.io.in.poke(2.U)
      dut.io.sel.poke(0.U)
      dut.clock.step()
      dut.io.out.expect(0.U)
      dut.io.out2.expect(0.U)
      dut.io.sel.poke(1.U)
      dut.clock.step()
      dut.io.out.expect(1.U)
      dut.io.out2.expect(1.U)
    }
  }
  "IfElse" should "pass" in {
    test(new IfElseTop()).withAnnotations(Seq(VerilatorBackendAnnotation)) { dut =>
      dut.io.in1.poke(1.U)
      dut.io.in2.poke(2.U)
      dut.io.in3.poke(3.U)
      dut.io.c1.poke(1.B)
      dut.io.c2.poke(0.B)
      dut.clock.step()
      dut.io.out.expect(1.U)
      dut.io.out2.expect(1.U)
      dut.io.c1.poke(0.B)
      dut.io.c2.poke(1.B)
      dut.clock.step()
      dut.io.out.expect(2.U)
      dut.io.out2.expect(2.U)
      dut.io.c1.poke(0.B)
      dut.io.c2.poke(0.B)
      dut.clock.step()
      dut.io.out.expect(3.U)
      dut.io.out2.expect(3.U)
    }
  }
}
