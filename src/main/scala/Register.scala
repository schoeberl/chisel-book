import chisel3._

class Register extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(8.W))
    val out = Output(UInt(8.W))
  })

  //- start register
  val reg = RegInit(0.U(8.W))
  //- end
  reg := io.in

  io.out := reg
}
