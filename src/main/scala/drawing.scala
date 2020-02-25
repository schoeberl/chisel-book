import chisel3._
import chisel3.util._

// Some Chisel code as drawing exercise
// TODO: write tester and include it in the Chisel book

class DrawAcc extends Module {
  val io = IO(new Bundle {
    val din = Input(UInt(8.W))
    val sel = Input(UInt(3.W))
    val dout = Output(UInt(8.W))
  })

  val sel = io.sel
  val din = io.din

  //- start draw_acc
  val regAcc = RegInit(0.U(8.W))

  switch(sel) {
    is(0.U) { regAcc := regAcc}
    is(1.U) { regAcc := 0.U}
    is(2.U) { regAcc := regAcc + din}
    is(3.U) { regAcc := regAcc - din}
  }
  //- end

  io.dout := regAcc
}

class DrawMux6 extends Module {
  val io = IO(new Bundle {
    val sel = Input(UInt(3.W))
    val dout = Output(UInt(8.W))
  })

  val sel = io.sel

  //- start draw_mux6
  val dout = WireDefault(0.U)

  switch(sel) {
    is(0.U) { dout := 0.U }
    is(1.U) { dout := 11.U }
    is(2.U) { dout := 22.U }
    is(3.U) { dout := 33.U }
    is(4.U) { dout := 44.U }
    is(5.U) { dout := 55.U }
  }
  //- end

  io.dout := dout
}