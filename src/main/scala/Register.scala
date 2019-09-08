import chisel3._

class Register extends Module {
  val io = IO(new Bundle {
    val in = Input(UInt(8.W))
    val out = Output(UInt(8.W))
  })

  //- start register
  //- 开始寄存器
  val reg = RegInit(0.U(8.W))
  //- end
  //- 结束

  val d = io.in
  //- start reg_con
  //- 开始寄存器的输入
  reg := d
  val q = reg
  //- end
  //- 结束

  //- start reg_next
  //- 开始寄存器的下一状态
  val nextReg = RegNext(d)
  //- end
  //- 结束

  //- start reg_both
  //- 开始寄存器的初始化和下一状态的表述方式
  val bothReg = RegNext(d, 0.U)
  //- end
  //- 结束
  io.out := reg
}
