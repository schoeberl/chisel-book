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
  // 可能信号应该是Bool类型？
  // FIXME: WireInit shall become WireDefault
  // 修改：初始wire应该变为默认wire
  //- start flasher_fsm
  //- 开始 闪烁器的有限状态机
  val off :: flash1 :: space1 :: flash2 :: space2 :: flash3 :: Nil = Enum(6)
  val stateReg = RegInit(off)

  // FSM output
  // 有限状态机的输出
  val light = WireInit(false.B) 

  // Timer connection
  // 连接计时器
  // start timer with a load
  // 开始计时器通过读取
  val timerLoad = WireInit(false.B) 
  // select 6 or 4 cycles
  // 选择4或是6周期
  val timerSelect = WireInit(true.B) 
  val timerDone = Wire(Bool())

  timerLoad := timerDone

  // Master FSM
  // 主FSM
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
  //- 结束

  //- start flasher_timer
  //- 开始闪烁器的计时器
  val timerReg = RegInit(0.U)
  timerDone := timerReg === 0.U

  // Timer FSM (down counter)
  // 计时器的FSM(向下计时)
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
  //- 结束

  io.light := light
}

class Flasher2 extends FlasherBase {

  val start = io.start.toBool

  //- start flasher2_fsm
  //- 开始闪烁器2的FSM
  val off :: flash :: space :: Nil = Enum(3)
  val stateReg = RegInit(off)

  // FSM output
  // FSM输出
  val light = WireInit(false.B) 

  // Timer connection
  // 计时器连接

  // start timer with a load
  // 通过读取开始计时器
  val timerLoad = WireInit(false.B) 
  // select 6 or 4 cycles
  // 选择6或4周期
  val timerSelect = WireInit(true.B) 
  val timerDone = Wire(Bool())
  // Counter connection
  // 连接计数器
  val cntLoad = WireInit(false.B)
  val cntDecr = WireInit(false.B)
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
  //- 结束

  //- start flasher2_counter
  //- 开始闪烁器2的计数器
  val cntReg = RegInit(0.U)
  cntDone := cntReg === 0.U

  // Down counter FSM
  // 向下计数FSM
  when(cntLoad) { cntReg := 2.U }
  when(cntDecr) { cntReg := cntReg - 1.U }
  //- end
  //- 结束

  val timerReg = RegInit(0.U)
  timerDone := timerReg === 0.U

  // Timer FSM (down counter)
  // 计时器FSM(向下计数)

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
