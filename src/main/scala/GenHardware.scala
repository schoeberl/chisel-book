import chisel3._

class GenHardware extends Module {
  val io = IO(new Bundle {
    val data = Output(Vec(12, UInt(8.W)))
    val len = Output(UInt(8.W))
    val squareIn = Input(UInt(8.W))
    val squareOut = Output(UInt(8.W))
  })

  //- start hello_table
  val msg = "Hello World!"
  val text = VecInit(msg.map(_.U))
  val len = msg.length.U
  //- end

  val n = io.squareIn
  //- start gen_vec
  val squareROM = VecInit(0.U, 1.U, 4.U, 9.U, 16.U, 25.U)
  val square = squareROM(n)
  //- end



  io.data := text
  io.len := len
  io.squareOut := square
}
