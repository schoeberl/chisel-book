import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec
//- start boring_import
import chisel3.util.experimental.BoringUtils
//- end

//- start boring_tickgen
class TickGen extends Module {
  val io = IO(new Bundle {
    val tick = Output(Bool())
  })

  val cntReg = RegInit(0.U(8.W))
  cntReg := cntReg + 1.U
  io.tick := cntReg === 9.U
  when(io.tick) {
    cntReg := 0.U
  }
}
//- end

//- start boring_tickgen_test_top
class TickGenTestTop extends Module {
  val io = IO(new Bundle {
    val tick = Output(Bool())
    val counter = Output(UInt(8.W))
  })

  val tickGen = Module(new TickGen)
  io.tick := tickGen.io.tick
  io.counter := DontCare
  BoringUtils.bore(tickGen.cntReg, Seq(io.counter))
}
//- end
class BoringTest extends AnyFlatSpec with ChiselScalatestTester {
  "Boring" should "work" in {
    //- start boring_tickgen_test
    test(new TickGenTestTop()) { dut =>
      dut.io.tick.expect(false.B)
      dut.io.counter.expect(0.U)

      dut.clock.step()
      dut.io.tick.expect(false.B)
      dut.io.counter.expect(1.U)

      dut.clock.step(8)
      dut.io.tick.expect(true.B)
      dut.io.counter.expect(9.U)

      dut.clock.step()
      dut.io.tick.expect(false.B)
      dut.io.counter.expect(0.U)
    }
    //- end
  }
}