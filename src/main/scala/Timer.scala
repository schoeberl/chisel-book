import chisel3._

class Timer extends Module {
  val io = IO(new Bundle {
    val din = Input(UInt(8.W))
    val load = Input(Bool())
    val done = Output(Bool())
  })

  val din = io.din
  val load = io.load
  //- start timer
  val cntReg = RegInit(0.U(8.W))
  val done = cntReg === 0.U

  val next = WireInit(0.U)
  when (load) {
    next := din
  } .elsewhen (!done) {
    next := cntReg - 1.U
  }
  cntReg := next
  //- end
  // printf("%d %d %d\n", next, reg, done)

  io.done := done
}
