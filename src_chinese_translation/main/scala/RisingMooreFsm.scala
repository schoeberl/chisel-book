//- start rising_moore_fsm
//- 开始上沿输出的摩尔FSM
import chisel3._
import chisel3.util._

class RisingMooreFsm extends Module {
  val io = IO(new Bundle{
    val din = Input(Bool())
    val risingEdge = Output(Bool())
  })

  // The three states
  // 三种状态
  val zero :: puls :: one :: Nil = Enum(3)

  // The state register
  // 状态寄存器
  val stateReg = RegInit(zero)

  // Next state logic
  // 下一个状态逻辑
  switch (stateReg) {
    is(zero) {
      when(io.din) {
        stateReg := puls
      }
    }
    is(puls) {
      when(io.din) {
        stateReg := one
      } .otherwise {
        stateReg := zero
      }
    }
    is(one) {
      when(!io.din) {
        stateReg := zero
      }
    }
  }

  // Output logic
  // 输出逻辑
  io.risingEdge := stateReg === puls
}
//- end
//- 结束

