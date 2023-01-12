import chisel3._
import chisel3.util._

class FunctionalAdd extends Module {
  val io = IO(new Bundle {
    val in = Input(Vec(5, UInt(10.W)))
    val res = Output(UInt(10.W))
  })

  val vec = io.in

  /*
  //- start fun_first
  def add(a: UInt, b:UInt) = a + b

  val sum = vec.reduce(add)
  //- end
   */

  // reduce is a reduceLeft

  /*
  //- start fun_func_lit
  val sum = vec.reduce(_ + _)
  //- end
   */

  //- start fun_reduce_tree
  val sum = vec.reduceTree(_ + _)
  //- end

  io.res := sum
}

class FunctionalComp extends Module {
  val io = IO(new Bundle() {
    val a = Input(UInt(8.W))
    val b = Input(UInt(8.W))
    val equ = Output(UInt(8.W))
    val gt = Output(UInt(8.W))
  })

  val inA = io.a
  val inB = io.b

  //- start fun_comp
  def compare(a: UInt, b: UInt) = {
    val equ = a === b
    val gt = a > b
    (equ, gt)
  }
  //- end

  //- start fun_comp_use1
  val cmp = compare(inA, inB)
  val equResult = cmp._1
  val gtResult = cmp._2
  //- end

  //- start fun_comp_use2
  val (equ, gt) = compare(inA, inB)
  //- end

  io.equ := equResult
  io.gt := gtResult

  io.equ := equ
  io.gt := gt



}

class FunctionalMin(n: Int, w: Int) extends Module {
  val io = IO(new Bundle {
    val in = Input(Vec(n, UInt(w.W)))
    val min = Output(UInt(w.W))
    val resA = Output(UInt(w.W))
    val idxA = Output(UInt(8.W))
    val resB = Output(UInt(w.W))
    val idxB = Output(UInt(8.W))
    val resC = Output(UInt(w.W))
    val idxC = Output(UInt(8.W))
  })



  // class X (v: UInt(w.W), idx: UInt) extends Bundle

  // that would be the real functional thing with a tuple
  // def foo(x: (UInt, UInt), y: (UInt, UInt)) = Mux((x._1 < y._1), x, y)
  // TODO: I don't have the functional version right now to map a Vec to the tuple thing
  // doesn't like tuples here :-(
  // val inDup = Wire(Vec(n, (UInt, UInt)))

  val vec = io.in

  //- start fun_min
  val min = vec.reduceTree((x, y) => Mux(x < y, x, y))
  //- end

  //- start fun_min2
  class Two extends Bundle {
    val v = UInt(w.W)
    val idx = UInt(8.W)
  }

  val vecTwo = Wire(Vec(n, new Two()))
  for (i <- 0 until n) {
    vecTwo(i).v := vec(i)
    vecTwo(i).idx := i.U
  }

  val res = vecTwo.reduceTree((x, y) => Mux(x.v < y.v, x, y))
  //- end

  //- start fun_min3
  val resFun = vec.zipWithIndex
    .map((x) => (x._1, x._2.U))
    .reduce((x, y) => (Mux(x._1 < y._1, x._1, y._1), Mux(x._1 < y._1, x._2, y._2)))
  //- end


  // An implicit conversion, can be used as:
  // val x = scalaVector.toVec
  implicit class ToMixedVecConv[T <: Data](seq: Seq[T]) {
    def toVec: Vec[T] = VecInit(seq)
  }

  //- start fun_min4
  val scalaVector = vec.zipWithIndex
    .map((x) => MixedVecInit(x._1, x._2.U(8.W)))
  val resFun2 = VecInit(scalaVector)
    .reduceTree((x, y) => Mux(x(0) < y(0), x, y))

  val minVal = resFun2(0)
  val minIdx = resFun2(1)
  //- end

  io.min := min

  io.resA := res.v
  io.idxA := res.idx

  io.resB := resFun._1
  io.idxB := resFun._2

  io.resC := minVal
  io.idxC := minIdx
}

object ScalaFunctionalMin {

  def findMin(v: Seq[Int]) = {
    v.zip((0 until v.length).toList).reduce((x, y) => if (x._1 <= y._1) x else y)
  }
}

object FunctionalMin extends App {
  println(getVerilogString(new FunctionalMin(5, 8)))
}