import chisel3._

//- start bundle
//- 开始捆束
class Channel() extends Bundle {
  val data = UInt(32.W)
  val valid = Bool()
}
//- end
//- 结束

class BundleVec extends Module {
  val io = IO(new Bundle{
    val chan = Output(new Channel())
    val chan2 = Output(new Channel())
    val idx = Input(UInt(4.W))
    val array = Output(UInt(4.W))
    val dIn = Input(UInt(32.W))
    val dOut = Output(UInt(32.W))
    val bvOut = Output(UInt(8.W))
    val chreg = Output(new Channel())
  })

  //- start bundle_use
  //- 开始使用捆束
  val ch = Wire(new Channel())
  ch.data := 123.U
  ch.valid := true.B

  val b = ch.valid
  //- end
  //- 结束

  //- start bundle_ref
  //- 开始引用捆束
  val channel = ch
  //- end
  //-结束

  //- start vec
  //- 开始向量
  val v = Wire(Vec(3, UInt(4.W)))
  //- end
  //- 结束

  //- start vec_access
  //- 开始进入向量
  v(0) := 1.U
  v(1) := 3.U
  v(2) := 5.U

  val idx = 1.U(2.W)
  val a = v(idx)
  //- end
  //- 结束

  io.array := v(io.idx)
  io.chan := ch

  //- start reg_file
  //- 开始寄存器文件
  val registerFile = Reg(Vec(32, UInt(32.W)))
  //- end
  //- 结束

  val dIn = io.dIn

  //- start reg_file_access
  //- 开始寄存器文件读取
  registerFile(idx) := dIn
  val dOut = registerFile(idx)
  //- end
  //- 结束

  io.dOut := dOut

  //- start vec_bundle
  //- 开始捆束的向量
  val vecBundle = Wire(Vec(8, new Channel()))
  //- end
  //- 结束

  for (i <- 0 to 7) {
    vecBundle(i) := ch
  }
  io.chan2 := vecBundle(1)

  //- start bundle_vec
  //- 开始向量的捆束
  class BundleVec extends Bundle {
    val field = UInt(8.W)
    val vector = Vec(4,UInt(8.W))
  }
  //- end
  //- 结束

  val bv = Wire(new BundleVec())
  bv.field := 13.U
  for (i <- 0 to 3) {
    bv.vector(i) := 3.U
  }

  io.bvOut := bv.field

  //- start bundle_reg_init
  //- 开始寄存器的捆束初始化
  val initVal = Wire(new Channel())

  initVal.data := 0.U
  initVal.valid := false.B

  val channelReg = RegInit(initVal)
  //- end
  //- 结束

  channelReg.data := 1.U
  channelReg.valid := true.B

  io.chreg := channelReg

}
