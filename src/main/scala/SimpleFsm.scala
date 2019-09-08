//- start simple_fsm
//- 开始简单fsm
import chisel3._
import chisel3.util._

class SimpleFsm extends Module {
  val io = IO(new Bundle{
    val badEvent = Input(Bool())
    val clear = Input(Bool())
    val ringBell = Output(Bool())
  })

  // The three states
  // 第三阶段
  val green :: orange :: red :: Nil = Enum(3)

  // The state register
  // 状态寄存器
  val stateReg = RegInit(green)

  // Next state logic
  // 下个状态逻辑
  switch (stateReg) {
    is (green) {
      when(io.badEvent) {
        stateReg := orange
      }
    }
    is (orange) {
      when(io.badEvent) {
        stateReg := red
      } .elsewhen(io.clear) {
        stateReg := green
      }
    }
    is (red) {
      when (io.clear) {
        stateReg := green
      }
    }
  }

  // Output logic
  // 输出逻辑
  io.ringBell := stateReg === red
}
//- end
//- 结束

class SimpleFsmCopy extends Module {
  //- start simple_fsm_io
  //- 开始简单fsm端口
  val io = IO(new Bundle{
    val badEvent = Input(Bool())
    val clear = Input(Bool())
    val ringBell = Output(Bool())
  })
  //- end
  //- 结束

  //- start simple_fsm_states
  //- 开始简单fsm状态
  val green :: orange :: red :: Nil = Enum(3)
  //- end
  //- 结束

  //- start simple_fsm_register
  //- 开始简单fsm寄存器
  val stateReg = RegInit(green)
  //- end
  //- 结束

  //- start simple_fsm_next
  //- 开始简单fsm的转移状态
  switch (stateReg) {
    is (green) {
      when(io.badEvent) {
        stateReg := orange
      }
    }
    is (orange) {
      when(io.badEvent) {
        stateReg := red
      } .elsewhen(io.clear) {
        stateReg := green
      }
    }
    is (red) {
      when (io.clear) {
        stateReg := green
      }
    }
  }
  //- end
  //- 结束

  //- start simple_fsm_output
  //- 开始简单fsm输出
  io.ringBell := stateReg === red
  //- end
  //- 结束
}
