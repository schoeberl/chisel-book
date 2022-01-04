import chisel3._
import chiseltest._
import org.scalatest._
import ScalaFunctionalMin._
import org.scalatest.flatspec.AnyFlatSpec

class FunctionalMinTester extends AnyFlatSpec with ChiselScalatestTester {
  "ScalaFunctionalMin" should "pass" in {
     assert(findMin(List(1, 0, 3, 2, 0, 5)) == (0, 1))
  }

  "FunctionalMin" should "pass" in {
    test(new FunctionalMin(5, 8)) { d =>
      d.io.in(0).poke(3.U)
      d.io.in(1).poke(5.U)
      d.io.in(2).poke(1.U)
      d.io.in(3).poke(7.U)
      d.io.in(4).poke(3.U)
      d.clock.step()
      d.io.min.expect(1.U)
      d.io.resA.expect(1.U)
      d.io.idxA.expect(2.U)
      d.io.resB.expect(1.U)
      d.io.idxB.expect(2.U)
      d.io.resC.expect(1.U)
      d.io.idxC.expect(2.U)
    }
  }

  "FunctionalMin random tests" should "pass" in {
    val r = new scala.util.Random()
    for (n <- 0 until 5) {
      val size = r.nextInt(10) + 1
      test(new FunctionalMin(size, 32)) { d =>
        val vals = new Array[Int](size)
        for (i <- 0 until size) {
          val v = r.nextInt(2000000)
          vals(i) = v
          d.io.in(i).poke(v.U)
        }
        d.clock.step()
        val min = vals.reduce((x, y) => x min y)
        val idx = vals.indexOf(min)
        d.io.min.expect(min.U)
        d.io.resA.expect(min.U)
        d.io.idxA.expect(idx.U)
        d.io.resB.expect(min.U)
        d.io.idxB.expect(idx.U)
        d.io.resC.expect(min.U)
        d.io.idxC.expect(idx.U)
      }
    }
  }
}
