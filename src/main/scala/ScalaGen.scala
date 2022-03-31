import chisel3._

class ScalaGen extends Module {
  val io = IO(new Bundle {
    val din = Input(UInt(1.W))
    val dout = Output(UInt(8.W))
  })

  //- start scala_loop_gen
  val regVec = Reg(Vec(8, UInt(1.W)))

  regVec(0) := io.din
  for (i <- 1 until 8) {
    regVec(i) := regVec(i-1)
  }
  //- end

  io.dout := regVec.asUInt
}
