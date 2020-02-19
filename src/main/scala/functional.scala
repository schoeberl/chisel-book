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
