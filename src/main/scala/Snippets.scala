// Just code snippets for syntax check by the compiler

import chisel3._

class BasicExercise extends Module {
  //- start basic_exercise
  val io = IO(new Bundle {
    val sw = Input(UInt(2.W))
    val led = Output(UInt(1.W))
  })
  //- end
  io.led := io.sw(0) & io.sw(1)
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


class Adder extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(4.W))
    val b = Input(UInt(4.W))
    val result = Output(UInt(4.W))
  })

  val addVal = io.a + io.b
  io.result := addVal
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

object Snippets extends App {
  chisel3.Driver.execute(Array("--target-dir", "generated"), () => new Conditional())
}