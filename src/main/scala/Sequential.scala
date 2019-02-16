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
    val riseIn = Input(UInt(1.W))
    val riseOut = Output(UInt(1.W))
  })

  val d = io.d
  //- start sequ_reg
  val q = RegNext(d)
  //- end
  io.q := q

  val delayIn = io.d2
  //- start sequ_reg2
  val regDelay = Reg(UInt(4.W))

  regDelay := delayIn
  //- end
  io.q2 := regDelay

  val inVal = io.d3
  //- start sequ_reg_init
  val regVal = RegInit(0.U(4.W))

  regVal := inVal
  //- end
  io.q3 := regVal

  val enable = io.ena
  //- start sequ_reg_ena
  val regEnable = Reg(UInt(4.W))

  when (enable) {
    regEnable := inVal
  }
  //- end
  io.q4 := regEnable

  //- start sequ_reg_init_ena
  val regResetEnable = RegInit(0.U(4.W))

  when (enable) {
    regResetEnable := inVal
  }
  //- end
  io.q5 := regResetEnable

  val din = io.riseIn
  //- start sequ_reg_expr
  val risingEdge = din & !RegNext(din)
  //- end
  io.riseOut := risingEdge
}


class SequCounter extends Module {
  val io = IO(new Bundle {
    val out = Output(UInt(4.W))
    val event = Input(Bool())
    val eventCnt = Output(UInt(4.W))
    val tick = Output(Bool())
    val lowCnt = Output(UInt(4.W))

  })

  //- start sequ_free_counter
  val regCnt = RegInit(0.U(4.W))

  regCnt := regCnt + 1.U
  //- end
  io.out := regCnt

  val event = io.event
  //- start sequ_event_counter
  val regEvents = RegInit(0.U(4.W))
  when(event) {
    regEvents := regEvents + 1.U
  }
  //- end
  io.eventCnt := regEvents

  val N = 5
  //- start sequ_tick_gen
  val regTickCounter = RegInit(0.U(4.W))
  val tick = regTickCounter === (N-1).U

  regTickCounter := regTickCounter + 1.U
  when (tick) {
    regTickCounter := 0.U
  }
  //- end

  //- start sequ_tick_counter
  val regLowFrequCnt = RegInit(0.U(4.W))
  when (tick) {
    regLowFrequCnt := regLowFrequCnt + 1.U
  }
  //- end

  io.tick := tick
  io.lowCnt := regLowFrequCnt
}