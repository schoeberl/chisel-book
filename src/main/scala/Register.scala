import chisel3._

class Register extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(8.W))
    val out = Output(UInt(8.W))
  })

  //- start register
  val reg = RegInit(0.U(8.W))
  //- end

  val d = io.in
  //- start reg_con
  reg := d
  val q = reg
  //- end

  //- start reg_next
  val nextReg = RegNext(d)
  //- end

  //- start reg_both
  val bothReg = RegNext(d, 0.U)
  //- end
  io.out := reg
}
