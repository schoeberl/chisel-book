import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class RegisterFileTest extends AnyFlatSpec with ChiselScalatestTester {
  "RegistersFile" should "have a debug port" in {
    test(new RegisterFile(true)) { dut =>
      dut.io.rs1.poke(4.U)
      dut.io.rs2.poke(2.U)
      dut.io.rd.poke(4.U)
      dut.io.wrData.poke(123.U)
      dut.io.wrEna.poke(true.B)
      dut.clock.step()
      dut.io.rs1Val.expect(123.U)
      //- start register_file_test
      dut.io.dbgPort.get(4).expect(123.U)
      //- end
    }
  }
  // TODO: test if exception is raised
  // java.util.NoSuchElementException: None.get
  "RegistersFile" should "rise and exception without the debug port" in {
    test(new RegisterFile(false)) { dut =>
      dut.io.rs1.poke(4.U)
      dut.io.rs2.poke(2.U)
      dut.io.rd.poke(4.U)
      dut.io.wrData.poke(123.U)
      dut.io.wrEna.poke(true.B)
      dut.clock.step()
      dut.io.rs1Val.expect(123.U)
      // dut.io.dbgPort.get(4).expect(123.U)
    }
  }

}
