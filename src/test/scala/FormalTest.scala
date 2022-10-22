
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

  val result = VecInit(Seq.fill(width)(false.B))
  for (i <- 0 until width) {
    when (a(i) && b(i)) {
      result(i) := true.B
    }
  }

  val resUInt = result.asUInt

  assert(m.io.y === resUInt)
}

class FormalTest extends AnyFlatSpec with ChiselScalatestTester with Formal {

  "FormalSimple" should "pass" in {
    verify(new FormalSimpleTest(8), Seq(BoundedCheck(1)))
  }
}