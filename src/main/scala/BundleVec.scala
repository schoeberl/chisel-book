import chisel3._

//- start bundle
class Channel() extends Bundle {
  val data = UInt(32.W)
  val valid = Bool()
}
//- end

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
    val din = Input(UInt(8.W))
    val dout = Output(UInt(8.W))
    val rdIdx = Input(UInt(2.W))
    val wrIdx = Input(UInt(2.W))
    val selMux = Input(UInt(2.W))
    val muxOut = Output(UInt(8.W))
  })

  //- start bundle_use
  val ch = Wire(new Channel())
  ch.data := 123.U
  ch.valid := true.B

  val b = ch.valid
  //- end

  //- start bundle_ref
  val channel = ch
  //- end

  //- start vec
  val v = Wire(Vec(3, UInt(4.W)))
  //- end

  //- start vec_access
  v(0) := 1.U
  v(1) := 3.U
  v(2) := 5.U

  val index = 1.U(2.W)
  val a = v(index)
  //- end

  io.array := v(io.idx)
  io.chan := ch

  val din = io.din
  val rdIdx = io.rdIdx
  val wrIdx = io.wrIdx
  //- start vec_reg
  val regVec = Reg(Vec(3, UInt(8.W)))

  val dout = regVec(rdIdx)
  regVec(wrIdx) := din
  //- end
  io.dout := dout

  io.muxOut := 3.U
  val x = 0.U
  val y = 11.U
  val z = 22.U
  val select = io.selMux
  //- start vec_mux
  val vec = Wire(Vec(3, UInt(8.W)))
  vec(0) := x
  vec(1) := y
  vec(2) := z
  val muxOut = vec(select)
  //- end
  io.muxOut := muxOut



  //- start reg_file
  val registerFile = Reg(Vec(32, UInt(32.W)))
  //- end

  val dIn = io.dIn

  //- start reg_file_access
  registerFile(index) := dIn
  val dOut = registerFile(index)
  //- end

  io.dOut := dOut

  //- start vec_bundle
  val vecBundle = Wire(Vec(8, new Channel()))
  //- end

  for (i <- 0 to 7) {
    vecBundle(i) := ch
  }
  io.chan2 := vecBundle(1)

  //- start bundle_vec
  class BundleVec extends Bundle {
    val field = UInt(8.W)
    val vector = Vec(4,UInt(8.W))
  }
  //- end

  val bv = Wire(new BundleVec())
  bv.field := 13.U
  for (i <- 0 to 3) {
    bv.vector(i) := 3.U
  }

  io.bvOut := bv.field

  //- start bundle_reg_init
  val initVal = Wire(new Channel())

  initVal.data := 0.U
  initVal.valid := false.B

  val channelReg = RegInit(initVal)
  //- end

  channelReg.data := 1.U
  channelReg.valid := true.B

  io.chreg := channelReg

}
