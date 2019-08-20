//- start rising_moore_fsm
import chisel3._
import chisel3.util._

class RisingMooreFsm extends Module {
  val io = IO(new Bundle{
    val din = Input(Bool())
    val risingEdge = Output(Bool())
  })

  // The three states
  val zero :: puls :: one :: Nil = Enum(3)

  // The state register
  val regState = RegInit(zero)

  // Next state logic
  switch (regState) {
    is(zero) {
      when(io.din) {
        regState := puls
      }
    }
    is(puls) {
      when(io.din) {
        regState := one
      } .otherwise {
        regState := zero
      }
    }
    is(one) {
      when(!io.din) {
        regState := zero
      }
    }
  }

  // Output logic
  io.risingEdge := regState === puls
}
//- end

