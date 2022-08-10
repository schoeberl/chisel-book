import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class ArbiterTreeTest extends AnyFlatSpec with ChiselScalatestTester {

  def testBasic[T <: Arbiter[_ <: UInt]](dut: T) = {
    for (i <- 0 until 4) {
      dut.io.in(i).valid.poke(false.B)
    }
    dut.io.out.ready.poke(false.B) // Keep the output till we read it
    dut.io.in(2).valid.poke(true.B)
    dut.io.in(2).bits.poke(2.U)
    while(!dut.io.in(2).ready.peek().litToBoolean) {
      dut.clock.step()
    }
    dut.clock.step()
    dut.io.in(2).valid.poke(false.B)
    dut.clock.step(10)
    dut.io.out.bits.expect(2.U)
  }

  "ArbiterTree" should "pass" in {
    test(new ArbiterTree(4, UInt(8.W))) { dut =>
      testBasic(dut)
    }
  }

  "ArbiterSimpleTree" should "pass" in {
    test(new ArbiterSimpleTree(4, UInt(8.W))) { dut =>
      testBasic(dut)
    }
  }

  // TODO: finish the test
  def testFair[T <: Arbiter[_ <: UInt]](dut: T) = {
    for (i <- 0 until 5) {
      dut.io.in(i).valid.poke(true.B)
      dut.io.in(i).bits.poke((i*100).U)
    }
    // println("Result should be " + List(0, 100, 200, 300, 400).sum)
    dut.io.out.ready.poke(true.B)
    dut.clock.step()
    for (i <- 0 until 40) {
      if (dut.io.out.valid.peek().litToBoolean) {
        println(dut.io.out.bits.peek().litValue)
      }
      dut.clock.step()
    }
  }

  "ArbiterTree" should "be fair" in {
    test(new ArbiterTree(5, UInt(16.W))) { dut => testFair(dut) }
  }

  /*
  "ArbiterSimpleTree" should "be fair" in {
    test(new ArbiterSimpleTree(5, UInt(16.W))) { dut => testFair(dut) }
  }

   */

  /*
  // This depends on an add in the node, find a better way to do this
  it should "be balanced" in {
    // This needs an add in each stage to see the results
    test(new Arbiter(5, UInt(16.W))).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      for (i <- 0 until 5) {
        dut.io.in(i).valid.poke(true.B)
        dut.io.in(i).bits.poke(0.U)
      }
      dut.io.out.ready.poke(true.B)
      dut.clock.step()
      var min = 999
      var max = 0
      for (i <- 0 until 40) {
        if (dut.io.out.valid.peek().litToBoolean) {
          val hops = dut.io.out.bits.peek().litValue().toInt
          println(hops)
          if (hops < min) min = hops
          if (hops > max) max = hops
        }
        dut.clock.step()
      }
      assert(max-min <= 1, "Tree shall have a maximum difference of one hop for each node")
    }
  }

   */
}
