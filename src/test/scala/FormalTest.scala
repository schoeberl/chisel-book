
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

class PastTest() extends Module {
  val io = IO(new Bundle() {
    val in = Input(UInt(8.W))
    // val out = Output(UInt(8.W))
  })

  val reg = RegNext(RegNext(io.in))
  assert(reg === past((past(io.in))))
}

class AssumeTest extends Module {
  val in = IO(Input(UInt(8.W)))
  val out = IO(Output(UInt(8.W)))
  out := in + 1.U
  assume(in > 3.U && in < 255.U)
  assert(out > 4.U)
  assert(out <= 255.U)
}

class FormalTest extends AnyFlatSpec with ChiselScalatestTester with Formal {

  "FormalSimple" should "pass" in {
    verify(new FormalSimpleTest(8), Seq(BoundedCheck(1)))
  }

  "PastTest" should "pass" in {
    verify(new PastTest(), Seq(BoundedCheck(5), WriteVcdAnnotation))
  }

  "AssumeTest" should "pass" in {
    verify(new AssumeTest(), Seq(BoundedCheck(5), WriteVcdAnnotation))
  }
}