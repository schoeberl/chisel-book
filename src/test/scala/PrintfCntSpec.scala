import chisel3.iotesters.PeekPokeTester
import org.scalatest._
import chiseltest._


class PrintfCntSpec extends FlatSpec with Matchers {

  "Printf" should "print" in {
    chisel3.iotesters.Driver(() => new PrintfCnt()) { c =>
      new PeekPokeTester(c) {

        for (i <- 0 until 8) {
          println("from tester: " + peek(c.io.out))
          step(1)
        }
      }
    }
  }
}

class PrintfCntSpec2 extends FlatSpec with ChiselScalatestTester with Matchers {

  "Printf in ChiselTest" should "pass" in {
    test(new PrintfCnt()) { c =>
      for (i <- 0 until 8) {
        println("from tester2: " + c.io.out.peek().litValue())
        c.clock.step()
      }
    }
  }
}