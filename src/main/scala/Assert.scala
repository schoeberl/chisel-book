import chisel3._

class Assert extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(8.W))
    val b = Input(UInt(8.W))
    val sum = Output(UInt(8.W))
  })

  val a = io.a
  val b = io.b
  val sum = Wire(UInt(8.W))
  io.sum := sum

  //- start assert
  sum := a + b

  assert(sum >= a)
  assert(sum >= b)
  assert(sum === a + b)
  //- end

}
