import chisel3._
import chisel3.util._

class FlasherBase extends Module {
  val io = IO(new Bundle {
    val start = Input(UInt(1.W))
    val light = Output(UInt(1.W))
  })
}

class Flasher extends FlasherBase {

  val start = io.start.toBool

  // Maybe signals should be Bool?
  //- start flasher_fsm
  val off :: flash1 :: space1 :: flash2 :: space2 :: flash3 :: Nil = Enum(6)
  val stateReg = RegInit(off)

  val light = WireDefault(false.B) // FSM output

  // Timer connection
  val timerLoad = WireDefault(false.B) // start timer with a load
  val timerSelect = WireDefault(true.B) // select 6 or 4 cycles
  val timerDone = Wire(Bool())

  timerLoad := timerDone

  // Master FSM
  switch(stateReg) {
    is(off) {
      timerLoad := true.B
      timerSelect := true.B
      when (start) { stateReg := flash1 }
    }
    is (flash1) {
      timerSelect := false.B
      light := true.B
      when (timerDone) { stateReg := space1 }
    }
    is (space1) {
      when (timerDone) { stateReg := flash2 }
    }
    is (flash2) {
      timerSelect := false.B
      light := true.B
      when (timerDone) { stateReg := space2 }
    }
    is (space2) {
      when (timerDone) { stateReg := flash3 }
    }
    is (flash3) {
      timerSelect := false.B
      light := true.B
      when (timerDone) { stateReg := off }
    }
  }
  //- end

  //- start flasher_timer
  val timerReg = RegInit(0.U)
  timerDone := timerReg === 0.U

  // Timer FSM (down counter)
  when(!timerDone) {
    timerReg := timerReg - 1.U
  }
  when (timerLoad) {
    when (timerSelect) {
      timerReg := 5.U
    } .otherwise {
      timerReg := 3.U
    }
  }
  //- end

  io.light := light
}

class Flasher2 extends FlasherBase {

  val start = io.start.toBool

  //- start flasher2_fsm
  val off :: flash :: space :: Nil = Enum(3)
  val stateReg = RegInit(off)

  val light = WireDefault(false.B) // FSM output

  // Timer connection
  val timerLoad = WireDefault(false.B) // start timer with a load
  val timerSelect = WireDefault(true.B) // select 6 or 4 cycles
  val timerDone = Wire(Bool())
  // Counter connection
  val cntLoad = WireDefault(false.B)
  val cntDecr = WireDefault(false.B)
  val cntDone = Wire(Bool())

  timerLoad := timerDone

  switch(stateReg) {
    is(off) {
      timerLoad := true.B
      timerSelect := true.B
      cntLoad := true.B
      when (start) { stateReg := flash }
    }
    is (flash) {
      timerSelect := false.B
      light := true.B
      when (timerDone & !cntDone) { stateReg := space }
      when (timerDone & cntDone) { stateReg := off }
    }
    is (space) {
      cntDecr := timerDone
      when (timerDone) { stateReg := flash }
    }
  }
  //- end

  //- start flasher2_counter
  val cntReg = RegInit(0.U)
  cntDone := cntReg === 0.U

  // Down counter FSM
  when(cntLoad) { cntReg := 2.U }
  when(cntDecr) { cntReg := cntReg - 1.U }
  //- end

  val timerReg = RegInit(0.U)
  timerDone := timerReg === 0.U

  // Timer FSM (down counter)
  when(!timerDone) {
    timerReg := timerReg - 1.U
  }
  when (timerLoad) {
    when (timerSelect) {
      timerReg := 5.U
    } .otherwise {
      timerReg := 3.U
    }
  }

  io.light := light
}
