
import chisel3._
import chiseltest._
import chiseltest.formal._
import org.scalatest.flatspec.AnyFlatSpec

class FormalSimpleTest(width: Int) extends Module {
  val a = IO(Input(UInt(width.W)))
  val b = IO(Input(UInt(width.W)))

  val m = Module(new FormalSimple(width))
  m.io.a := a
  m.io.b := b

  assert(m.io.y === (a & b))
}

class FormalTest extends AnyFlatSpec with ChiselScalatestTester with Formal {

  "FormalSimple" should "pass" in {
    verify(new FormalSimpleTest(16), Seq(BoundedCheck(10)))
  }
}