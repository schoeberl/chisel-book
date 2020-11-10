import chisel3._

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


class FunctionalMin(n: Int, w: Int) extends Module {
  val io = IO(new Bundle {
    val in = Input(Vec(n, UInt(w.W)))
    val res = Output(UInt(w.W))
    val idx = Output(UInt(8.W))
  })



  // class X (v: UInt(w.W), idx: UInt) extends Bundle

  //- start fun_min
  // that would be the real functional thing with a tuple
  // def foo(x: (UInt, UInt), y: (UInt, UInt)) = Mux((x._1 < y._1), x, y)

  // TODO: I don't have the functional version right now to map a Vec to the tuple thing

  // doesn't like tuples here :-(
  // val inDup = Wire(Vec(n, (UInt, UInt)))

  class Dup extends Bundle {
    val v = UInt(w.W)
    val idx = UInt(8.W)
  }

  val inDup = Wire(Vec(n, new Dup()))
  for (i <- 0 until n) {
    inDup(i).v := io.in(i)
    inDup(i).idx := i.U
  }

  // the less functional function
  // def min(x: Dup, y: Dup) = Mux(x.v < y.v, x, y)

  val res = inDup.reduceTree((x, y) => Mux(x.v < y.v, x, y))
  //- end


  case class Pair(v: UInt, idx: UInt)

  val v = io.in
  // val z = Vec((0 until 5).toList.map(x => x.U))
  val z = Wire(Vec(v.length, UInt(8.W)))
  for (i <- 0 until v.length) z(i) := i.U
  System.out.println(v)
  System.out.println(z)
  // This zip gives a Scala Vector, although the inputs are Chisel Vecs
  val d = v.zip(z)
  System.out.println(d)

  val h = v.zipWithIndex
  System.out.println(h)





  io.res := res.v
  io.idx := res.idx
}

object ScalaFunctionalMin {

  def findMin(v: Seq[Int]) = {
    v.zip((0 until v.length).toList).reduce((x, y) => if (x._1 <= y._1) x else y)
  }
}

object FunctionalMin extends App {
  println(chisel3.Driver.emitVerilog(new FunctionalMin(5, 8)))
}