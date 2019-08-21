import chisel3._

//- start ticker
abstract class Ticker(n: Int) extends Module {
  val io = IO(new Bundle{
    val tick = Output(Bool())
  })

//  val cntReg = Reg(UInt(8.W))
}
//- end

//- start when_ticker
class WhenTicker(n: Int) extends Ticker(n) {

  val N = (n-1).U


  val cntReg = RegInit(0.U(8.W))

  cntReg := cntReg + 1.U
  when(cntReg === N) {
    cntReg := 0.U
  }

  io.tick := cntReg === N
}
//- end