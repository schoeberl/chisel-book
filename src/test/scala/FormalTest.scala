
import chisel3._
import chiseltest._
//- start formal_import
import chiseltest.formal._
//- end
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

//- start formal_past_example
class PastTest() extends Module {
  val io = IO(new Bundle() {
    val in = Input(UInt(8.W))
    val out = Output(UInt(8.W))
  })

  val reg = RegNext(RegNext(io.in))
  assert(reg === past((past(io.in))))

  io.out := reg
}
//- end

//- start formal_assume_example
class AssumeTest extends Module {
  val in = IO(Input(UInt(8.W)))
  val out = IO(Output(UInt(8.W)))
  out := in + 1.U
  assume(in > 3.U && in < 255.U)
  assert(out > 4.U)
  assert(out <= 255.U)
}
//- end

/*
 needs to be integrated and tested

//- start formal_past
 when (past(in) === 10.U) {
    assert(out === 11.U)
  }
//- end
//- start formal_stability
  assert(x === past(x))
//- end
//- start formal_assume
  assume(x > 0.U)
//- end

 */

//- start formal_class
class FormalTest extends AnyFlatSpec with ChiselScalatestTester with Formal {
//- end
  "FormalSimple" should "pass" in {
    verify(new FormalSimpleTest(8), Seq(BoundedCheck(1)))
  }

  "PastTest" should "pass" in {
    verify(new PastTest(), Seq(BoundedCheck(5), WriteVcdAnnotation))
  }

  "AssumeTest" should "pass" in {
    verify(new AssumeTest(), Seq(BoundedCheck(5), WriteVcdAnnotation))
  }

  //- start formal_assert
  "AssertTest" should "pass" in {
    verify(new Assert(), Seq(BoundedCheck(5), WriteVcdAnnotation))
  }
  //- end
}