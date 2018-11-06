import chisel3._

class Counter extends Module {
  val io = IO(new Bundle {
    val cnt = Output(UInt(8.W))
  })

  //- start counter
  val cntReg = RegInit(0.U(8.W))

  cntReg := Mux(cntReg === 100.U, 0.U, cntReg + 1.U)
  //- end

  io.cnt := cntReg
}

class Counter2 extends Module {
  val io = IO(new Bundle {
    val cnt = Output(UInt(8.W))
  })

  val cntReg = RegInit(0.U(8.W))

  cntReg := cntReg + 1.U
  when(cntReg === 100.U) {
    cntReg := 0.U
  }

  io.cnt := cntReg
}

object Counter extends App {
  chisel3.Driver.execute(Array("--target-dir", "generated"), () => new Counter2())
}