import chisel3._

class Comparator extends  Module {
  val io = IO(new Bundle() {
    val a = Input(UInt(8.W))
    val b = Input(UInt(8.W))
    val equ = Output(Bool())
    val gt = Output(Bool())
  })


  val a = io.a
  val b = io. b
  //- start comparator
  val equ = a === b
  val gt = a > b
  //- end
  io.equ := equ
  io.gt := gt
}
