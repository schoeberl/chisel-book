//- start rising_fsm
//- 开始输出上沿波形的FSM
import chisel3._
import chisel3.util._

class RisingFsm extends Module {
  val io = IO(new Bundle{
    val din = Input(Bool())
    val risingEdge = Output(Bool())
  })

  // The two states
  // 两个状态
  val zero :: one :: Nil = Enum(2)

  // The state register
  // 状态寄存器
  val stateReg = RegInit(zero)

  // default value for output
  // 输出的默认值
  io.risingEdge := false.B

  // Next state and output logic
  // 下一个状态和输出逻辑
  switch (stateReg) {
    is(zero) {
      when(io.din) {
        stateReg := one
        io.risingEdge := true.B
      }
    }
    is(one) {
      when(!io.din) {
        stateReg := zero
      }
    }
  }
}
//- end
//- 结束

