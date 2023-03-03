package verilog

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class SimpleTest extends AnyFlatSpec with ChiselScalatestTester {
  "Simple" should "pass" in {
    test(new SimpleTop()).withAnnotations(Seq(VerilatorBackendAnnotation)) { dut =>
      dut.io.a.poke(1.U)
      dut.io.b.poke(3.U)
      dut.clock.step()
      dut.io.c.expect(4.U)
    }
  }
}
