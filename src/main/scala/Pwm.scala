import chisel3._
import chisel3.util._

class Pwm extends Module {
  val io = IO(new Bundle {
    val led = Output(UInt(4.W))
  })

  //- start pwm
  def pwm(max: Int, din: UInt) = {
    val cntReg = RegInit(0.U(log2Up(max).W))
    cntReg := Mux(cntReg === (max-1).U, 0.U, cntReg + 1.U)
    din > cntReg
  }

  val din = 3.U
  val dout = pwm(10, din)
  //- end

  //- start pwm_modulate
  val FREQ = 100000000 // a 100 MHz board
  val MAX = FREQ/1000  // 1 kHz

  val modReg = RegInit(0.U(32.W))

  val upReg = RegInit(true.B)

  when (modReg < FREQ.U && upReg) {
    modReg := modReg + 1.U
  } .elsewhen (modReg === FREQ.U) {
    upReg := false.B
  } .elsewhen (modReg > 0.U && !upReg) {
    modReg := modReg - 1.U
  } .otherwise { // 0
    upReg := true.B
  }

  val sig = pwm(MAX, modReg)

  //-end


  io.led := Cat(2.U, sig, dout)
}

object Pwm extends App {
  chisel3.Driver.execute(Array[String](), () => new Pwm())
}