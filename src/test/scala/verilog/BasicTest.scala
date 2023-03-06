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
    }
  }
}
