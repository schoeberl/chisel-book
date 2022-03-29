import chisel3._

class ClockChannel extends Bundle {
  val data = Output(UInt(32.W))
  val valid = Output(Bool())
}
class SourceClock extends Module {
  val io = IO(new Bundle() {
    val c = new ClockChannel()
  })

  val valid = RegInit(false.B)
  val cntReg = RegInit(0.U(32.W))
  val xReg = RegInit(0.U(2.W))
  xReg := xReg + 1.U
  // valid := !valid
  valid := xReg(1)
  val falling = !valid & RegNext(valid)
  when (falling) {
    cntReg := cntReg + 1.U
  }
  io.c.valid := valid
  io.c.data := cntReg
}

class SinkClock extends Module {
  val io = IO(new Bundle() {
    val c = Flipped(new ClockChannel())
    val errorCnt = Output(UInt(8.W))
  })

  val expectedCntReg = RegInit(0.U(32.W))
  val errCntReg = RegInit(0.U(8.W))
  val sampleReg = RegInit(0.U(32.W))

  val rising = io.c.valid & !RegNext(io.c.valid)

  when (rising) {
    when (expectedCntReg =/= io.c.data) {
      errCntReg := errCntReg + 1.U
    }
    sampleReg := io.c.data
    expectedCntReg := expectedCntReg + 1.U
  }

  io.errorCnt := errCntReg
}

class ClockDomains extends Module {
  val io = IO(new Bundle() {
    val errCnt = Output(UInt(8.W))
  })

  val s = Module(new SourceClock())
  val d = Module(new SinkClock())

  d.io.c <> s.io.c
  io.errCnt := d.io.errorCnt
}

object ClockDomains extends App {
  println(getVerilogString(new ClockDomains()))
}
