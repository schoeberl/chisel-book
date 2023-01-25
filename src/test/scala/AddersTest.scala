import chisel3._
import chiseltest._
import org.scalatest._
import scala.language.implicitConversions
import org.scalatest.flatspec.AnyFlatSpec

trait AbstractAdderTest {
  def testFn[T <: AbstractAdder](dut: T, w: Int = 32) = {
    // Default values
    dut.io.a.poke(0.U)
    dut.io.b.poke(0.U)
    dut.io.cin.poke(false.B)
    dut.io.c.expect(0.U)
    dut.io.cout.expect(false.B)

    // Run some random operations
    implicit def boolean2BigInt(b: Boolean) = if (b) BigInt(1) else BigInt(0)
    val rng = new scala.util.Random(42)
    val aOps = Array.fill(128) { BigInt(w, rng) }
    val bOps = Array.fill(128) { BigInt(w, rng) }
    val cins = Array.fill(128) { rng.nextBoolean() }
    val ress = aOps.zip(bOps).zip(cins).map(
      e => (e._1._1 + e._1._2 + e._2) & ((BigInt(1) << w) - 1)
    )
    val couts = aOps.zip(bOps).zip(cins).map(
      e => ((e._1._1 + e._1._2 + e._2) & (BigInt(1) << w)) != BigInt(0)
    )
    for (i <- 0 until 128) {
      dut.io.a.poke(aOps(i).U)
      dut.io.b.poke(bOps(i).U)
      dut.io.cin.poke(cins(i).B)
      dut.clock.step()
      dut.io.c.expect(ress(i).U)
      dut.io.cout.expect(couts(i).B)
    }
  }
}

class AddersTest extends AnyFlatSpec with ChiselScalatestTester with AbstractAdderTest {
  behavior of "Adders"

  it should "add numbers" in {
    test(new CarryRippleAdder) { dut => testFn(dut) }
    test(new CarrySelectAdder) { dut => testFn(dut) }
    test(new CarrySkipAdder) { dut => testFn(dut) }
    test(new CarryLookaheadAdder) { dut => testFn(dut) }
    test(new BasicPrefixAdder) { dut => testFn(dut) }
    test(new MinFanoutPrefixAdder) { dut => testFn(dut) }
  }

  it should "work with odd width" in {
    test(new CarryRippleAdder(17)) { dut => testFn(dut, 17) }
    test(new CarrySelectAdder(17)) { dut => testFn(dut, 17) }
    test(new CarrySkipAdder(17)) { dut => testFn(dut, 17) }
    test(new CarryLookaheadAdder(17)) { dut => testFn(dut, 17) }
    // `BasicPrefixAdder` and `MinFanoutPrefixAdder` currently only work with powers of 2 widths
  }
}
