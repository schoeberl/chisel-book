import chisel3._
import chiseltest._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

class ParamRouterTest extends AnyFlatSpec with ChiselScalatestTester {
  "ParamRouter" should "pass" in {
    test(new NocRouter[Payload](new Payload, 2)) { dut =>
      dut.io.inPort(0).data.poke(123.U)
      dut.io.inPort(1).data.poke(42.U)
      dut.io.outPort(1).data.expect(123.U)
      dut.io.outPort(0).data.expect(42.U)
    }
  }
}
