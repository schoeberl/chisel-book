import chisel3._
import chisel3.experimental.BundleLiterals._
import chiseltest._
import org.scalatest._
import scala.language.implicitConversions
import org.scalatest.flatspec.AnyFlatSpec

class BlackBoxesTest extends AnyFlatSpec with ChiselScalatestTester {
  behavior of "BlackBoxAdder"

  private[this] abstract class BlackBoxAdder extends Module {
    val io = IO(new BlackBoxAdderIO)
  }

  val rng = new scala.util.Random(42)

  def testFn[T <: BlackBoxAdder](dut: T) = {
    // Default values
    dut.io.a.poke(0.U)
    dut.io.b.poke(0.U)
    dut.io.cin.poke(false.B)
    dut.io.c.expect(0.U)
    dut.io.cout.expect(false.B)

    // Run some random tests
    implicit def boolean2BigInt(b: Boolean) = if (b) BigInt(1) else BigInt(0)
    val as = Array.fill(128) { BigInt(32, rng) }
    val bs = Array.fill(128) { BigInt(32, rng) }
    val cins = Array.fill(128) { rng.nextBoolean() }
    val cs = as.zip(bs).zip(cins).map(
      e => (e._1._1 + e._1._2 + e._2) & ((BigInt(1) << 32) - 1)
    )
    val couts = as.zip(bs).zip(cins).map(
      e => ((e._1._1 + e._1._2 + e._2) & (BigInt(1) << 32)) != BigInt(0)
    )
    for (i <- 0 until 128) {
      dut.io.pokePartial(
        (new BlackBoxAdderIO).Lit(_.a -> as(i).U, _.b -> bs(i).U, _.cin -> cins(i).B)
      )
      dut.clock.step()
      dut.io.expectPartial(
        (new BlackBoxAdderIO).Lit(_.c -> cs(i).U, _.cout -> couts(i).B)
      )
    }
  }

  it should "work inlined" in {
    class InlineAdder extends BlackBoxAdder {
      val adder = Module(new InlineBlackBoxAdder)
      io <> adder.io
    }
    test(new InlineAdder).withAnnotations(Seq(VerilatorBackendAnnotation)) {
      dut => testFn(dut)
    }
  }

  it should "work with path" in {
    class PathAdder extends BlackBoxAdder {
      val adder = Module(new PathBlackBoxAdder)
      io <> adder.io
    }
    test(new PathAdder).withAnnotations(Seq(VerilatorBackendAnnotation)) { 
      dut => testFn(dut)
    }
  }

  it should "work with resource" in {
    class ResourceAdder extends BlackBoxAdder {
      val adder = Module(new ResourceBlackBoxAdder)
      io <> adder.io
    }
    test(new ResourceAdder).withAnnotations(Seq(VerilatorBackendAnnotation)) { 
      dut => testFn(dut)
    }
  }

  /*
  //- start blackbox_named
  class InlineAdder extends Module {
    val io = IO(new BlackBoxAdderIO)
    val adder = Module(new InlineBlackBoxAdder)
    io <> adder.io
  }
  //- end

  //- start blackbox_anonymous
  test(new Module {
    val io = IO(new BlackBoxAdderIO)
    val adder = Module(new InlineBlackBoxAdder)
    io <> adder.io
  })
  //- end
  */
}
