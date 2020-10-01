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

  "FunctionalMin random tests" should "pass" in {
    val r = new scala.util.Random()
    for (n <- 0 until 10) {
      val size = r.nextInt(20) + 1
      test(new FunctionalMin(size, 32)) { d =>
        val vals = new Array[Int](size)
        for (i <- 0 until size) {
          val v = r.nextInt(2000000)
          vals(i) = v
          d.io.in(i).poke(v.U)
          print(v + " ")
        }
        d.clock.step()
        val min = vals.reduce((x, y) => x min y)
        val idx = vals.indexOf(min)
        println(" -> " + min + " at index " + idx)
        d.io.res.expect(min.U)
        d.io.idx.expect(idx.U)
      }
    }



  }

}
