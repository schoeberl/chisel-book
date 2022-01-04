import chisel3._
import chiseltest._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec

class FunctionalTest extends AnyFlatSpec with ChiselScalatestTester {
  "FunctionalAdd" should "pass" in {
    test(new FunctionalAdd) { dut =>
      Seq(3, 2, 0, 9, 1).zipWithIndex.foreach { elem =>
        dut.io.in(elem._2).poke(elem._1.U)
      }
      dut.clock.step()
      dut.io.res.expect(15.U)
    }
  }
}
