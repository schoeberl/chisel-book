import chisel3._
import chiseltest._
import org.scalatest._

class FunctionalMinTester extends FlatSpec with ChiselScalatestTester with Matchers {

  "FunctionalMin" should "pass" in {
    test(new FunctionalMin(5, 8)) { d =>
      d.io.in(0).poke(3.U)
      d.io.in(1).poke(5.U)
      d.io.in(2).poke(1.U)
      d.io.in(3).poke(7.U)
      d.io.in(4).poke(3.U)
      d.clock.step()
      d.io.res.expect(1.U)
      d.io.idx.expect(2.U)
    }
  }

}
