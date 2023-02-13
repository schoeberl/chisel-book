// Just code snippets for syntax check by the compiler
// TODO: maybe this should go and all used code placed into
// modules that are tested.

import chisel3._

class BasicExercise extends Module {
  //- start basic_exercise
  val io = IO(new Bundle {
    val sw = Input(UInt(2.W))
    val led = Output(UInt(1.W))
  })
  //- end

  // This is possible
  val + = 1.U
  val Module = 2.U
  val when = 3.U

  io.led := io.sw(0) & io.sw(1)
}

class NosensKeywords extends Module {
  val io = IO(new Bundle {
    val led = Output(UInt(1.W))
  })

  // This is possible, but does not make sense
  val + = 1.U
  val Module = 2.U
  val when = 3.U

}

class AluIO extends Bundle {
  val function = Input(UInt(2.W))
  val inputA = Input(UInt(4.W))
  val inputB = Input(UInt(4.W))
  val result = Output(UInt(4.W))
}

class DecodeExecute extends Bundle {
  val x = UInt(3.W)
}
class ExecuteMemory extends Bundle {
  val x = UInt(3.W)
}

class ExecuteIO extends Bundle {
  val dec = Input(new DecodeExecute())
  val mem = Output(new ExecuteMemory())
}

/*
class Channel extends Bundle {
  val data = Input(UInt(32.W))
  val ready = Output(Bool())
  val valid = Input(Bool())
}

class ChannelUsage extends Bundle {
  val input = new Channel()
  val output = Flipped(new Channel())
}
 */

class ParamChannel(n: Int) extends Bundle {
  val data = Input(UInt(n.W))
  val ready = Output(Bool())
  val valid = Input(Bool())
}



class Conditional extends Module {
  val io = IO(new Bundle {
    val condition = Input(Bool())
    val result = Output(UInt(4.W))
  })

  val condition = io.condition
  val c1 = ! condition
  val c2 = ! condition

  val v = Wire(UInt())

  v := 5.U
  when (condition) {
    v := 0.U
  }

  when (c1) { v := 1.U }
  when (c2) { v := 2.U }

  when (c1) { v := 1.U }
    .elsewhen (c2) { v := 2.U }
    .otherwise { v := 3.U }

  io.result := v
}

class Snippets extends Module {
  val io = IO(new AluIO())

  val cntReg = RegInit(0.U(8.W))

  cntReg := Mux(cntReg === 100.U,
    0.U, cntReg + 1.U)

  io.result := cntReg

  val ch32 = new ParamChannel(32)

  val add8 = Module(new ParamAdder(8))
}

class ExplainWireAndCo extends Module {
  val io = IO(new Bundle {
    val cond = Input(Bool())
    val out = Output(UInt(4.W))
  })

  //- start wire_reg
  val number = Wire(UInt())
  val reg = Reg(SInt())
  //- end

  val value = WireDefault(1.U)

  //- start wire_reg_reassign
  number := 10.U
  reg := value - 3.U
  //- end

}

class ExplainWireAndCo2 extends Module {
  val io = IO(new Bundle {
    val cond = Input(Bool())
    val out = Output(UInt(4.W))
  })

  //- start wire_reg_default
  val number = WireDefault(10.U(4.W))
  //- end

  //- start wire_reg_init
  val reg = RegInit(0.S(8.W))
  //- end
}

object Snippets extends App {
  emitVerilog(new Conditional(), Array("--target-dir", "generated"))
}