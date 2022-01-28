import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

//- start basic_chiseltest
class BcdTableTest extends AnyFlatSpec with ChiselScalatestTester {
  "BCD table" should "output BCD encoded numbers" in {
    test(new BcdTable) { dut =>
      dut.io.address.poke(0.U)
      dut.io.data.expect("h00".U)
      dut.io.address.poke(1.U)
      dut.io.data.expect("h01".U)
      dut.io.address.poke(13.U)
      dut.io.data.expect("h13".U)
      dut.io.address.poke(99.U)
      dut.io.data.expect("h99".U)
    }
  }
}
//- end

/*
//- start basic_chiseltest_it
class BcdTableTest extends FlatSpec with ChiselScalatestTester {
  behavior of "BCD table"

  it should "output BCD encoded numbers" in {
    test(new BcdTable) { dut =>
      ...
    }
  }
}
//- end
*/
