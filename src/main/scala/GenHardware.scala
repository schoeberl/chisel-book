import chisel3._

class GenHardware extends Module {
  val io = IO(new Bundle {
    val data = Output(Vec(12, UInt(8.W)))
    val len = Output(UInt(8.W))
  })

  //- start hello_table
  val msg = "Hello World!"
  val text = VecInit(msg.map(_.U))
  val len = msg.length.U
  //- end


  io.data := text
  io.len := len
}
