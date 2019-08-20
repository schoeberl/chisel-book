//- start rising_fsm
import chisel3._
import chisel3.util._

class RisingFsm extends Module {
  val io = IO(new Bundle{
    val din = Input(Bool())
    val risingEdge = Output(Bool())
  })

  // The two states
  val zero :: one :: Nil = Enum(2)

  // The state register
  val regState = RegInit(zero)

  // default value for output
  io.risingEdge := false.B

  // Next state and output logic
  switch (regState) {
    is(zero) {
      when(io.din) {
        regState := one
        io.risingEdge := true.B
      }
    }
    is(one) {
      when(!io.din) {
        regState := zero
      }
    }
  }
}
//- end

