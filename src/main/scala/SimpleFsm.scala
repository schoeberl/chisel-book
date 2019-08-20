//- start simple_fsm
import chisel3._
import chisel3.util._

class SimpleFsm extends Module {
  val io = IO(new Bundle{
    val badEvent = Input(Bool())
    val clear = Input(Bool())
    val ringBell = Output(Bool())
  })

  // The three states
  val green :: orange :: red :: Nil = Enum(3)

  // The state register
  val regState = RegInit(green)

  // Next state logic
  switch (regState) {
    is (green) {
      when(io.badEvent) {
        regState := orange
      }
    }
    is (orange) {
      when(io.badEvent) {
        regState := red
      } .elsewhen(io.clear) {
        regState := green
      }
    }
    is (red) {
      when (io.clear) {
        regState := green
      }
    }
  }

  // Output logic
  io.ringBell := regState === red
}
//- end

class SimpleFsmCopy extends Module {
  //- start simple_fsm_io
  val io = IO(new Bundle{
    val badEvent = Input(Bool())
    val clear = Input(Bool())
    val ringBell = Output(Bool())
  })
  //- end

  //- start simple_fsm_states
  val green :: orange :: red :: Nil = Enum(3)
  //- end

  //- start simple_fsm_register
  val regState = RegInit(green)
  //- end

  //- start simple_fsm_next
  switch (regState) {
    is (green) {
      when(io.badEvent) {
        regState := orange
      }
    }
    is (orange) {
      when(io.badEvent) {
        regState := red
      } .elsewhen(io.clear) {
        regState := green
      }
    }
    is (red) {
      when (io.clear) {
        regState := green
      }
    }
  }
  //- end

  //- start simple_fsm_output
  io.ringBell := regState === red
  //- end
}
