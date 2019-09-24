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
  val delayReg = Reg(UInt(4.W))

  delayReg := delayIn
  //- end
  io.q2 := delayReg

  val inVal = io.d3
  //- start sequ_reg_init
  val valReg = RegInit(0.U(4.W))

  valReg := inVal
  //- end
  io.q3 := valReg

  val enable = io.ena
  //- start sequ_reg_ena
  val enableReg = Reg(UInt(4.W))

  when (enable) {
    enableReg := inVal
  }
  //- end
  io.q4 := enableReg

  //- start sequ_reg_init_ena
  val resetEnableReg = RegInit(0.U(4.W))

  when (enable) {
    resetEnableReg := inVal
  }
  //- end
  io.q5 := resetEnableReg

  val din = io.riseIn
  //- start sequ_reg_rising
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
  val cntReg = RegInit(0.U(4.W))

  cntReg := cntReg + 1.U
  //- end
  io.out := cntReg

  val event = io.event
  //- start sequ_event_counter
  val cntEventsReg = RegInit(0.U(4.W))
  when(event) {
    cntEventsReg := cntEventsReg + 1.U
  }
  //- end
  io.eventCnt := cntEventsReg

  val N = 5
  //- start sequ_tick_gen
  val tickCounterReg = RegInit(0.U(4.W))
  val tick = tickCounterReg === (N-1).U

  tickCounterReg := tickCounterReg + 1.U
  when (tick) {
    tickCounterReg := 0.U
  }
  //- end

  //- start sequ_tick_counter
  val lowFrequCntReg = RegInit(0.U(4.W))
  when (tick) {
    lowFrequCntReg := lowFrequCntReg + 1.U
  }
  //- end

  io.tick := tick
  io.lowCnt := lowFrequCntReg
}