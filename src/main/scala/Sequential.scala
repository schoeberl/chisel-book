import chisel3._

class Sequential extends Module {
  val io = IO(new Bundle {
    val d = Input(UInt(4.W))
    val q = Output(UInt(4.W))
    val d2 = Input(UInt(4.W))
    val q2 = Output(UInt(4.W))
    val d3 = Input(UInt(4.W))
    val q3 = Output(UInt(4.W))
    val ena = Input(Bool())
    val q4 = Output(UInt(4.W))
    val q5 = Output(UInt(4.W))
  })

  val d = io.d
  //- start sequ_reg
  val q = RegNext(d)
  //- end
  io.q := q

  val delayIn = io.d2
  //- start sequ_reg2
  val regDelay = Reg(UInt())

  regDelay := delayIn
  //- end
  io.q2 := regDelay

  val inVal = io.d3
  //- start sequ_reg_init
  val regVal = RegInit(0.U)

  regVal := inVal
  //- end
  io.q3 := regVal

  val enable = io.ena
  //- start sequ_reg_ena
  val regEnable = Reg(UInt())

  when (enable) {
    regEnable := inVal
  }
  //- end
  io.q4 := regEnable

  //- start sequ_reg_init_ena
  val regResetEnable = RegInit(0.U)

  when (enable) {
    regResetEnable := inVal
  }
  //- end
  io.q5 := regResetEnable
}


class SequCounter extends Module {
  val io = IO(new Bundle {
    val q = Output(UInt(4.W))
  })

  //- start sequ_counter
  //- end

}