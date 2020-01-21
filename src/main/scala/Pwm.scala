import chisel3._
import chisel3.util._

class Pwm extends Module {
  val io = IO(new Bundle {
    val led = Output(UInt(4.W))
  })

  //- start pwm
  def pwm(nrCycles: Int, din: UInt) = {
    val cntReg = RegInit(0.U(unsignedBitLength(nrCycles-1).W))
    cntReg := Mux(cntReg === (nrCycles-1).U, 0.U, cntReg + 1.U)
    din > cntReg
  }

  val din = 3.U
  val dout = pwm(10, din)
  //- end

  //- start pwm_modulate
  val FREQ = 100000000 // a 100 MHz clock input
  val MAX = FREQ/1000  // 1 kHz

  val modulationReg = RegInit(0.U(32.W))

  val upReg = RegInit(true.B)

  when (modulationReg < FREQ.U && upReg) {
    modulationReg := modulationReg + 1.U
  } .elsewhen (modulationReg === FREQ.U && upReg) {
    upReg := false.B
  } .elsewhen (modulationReg > 0.U && !upReg) {
    modulationReg := modulationReg - 1.U
  } .otherwise { // 0
    upReg := true.B
  }

  // divide modReg by 1024 (about the 1 kHz)
  val sig = pwm(MAX, modulationReg >> 10)
  //- end


  io.led := Cat(0.U, sig, dout)
}

object Pwm extends App {
  chisel3.Driver.execute(Array[String](), () => new Pwm())
}