import chisel3._
import chisel3.util._

class ShiftRegister extends Module {
  val io = IO(new Bundle {
    val din = Input(UInt(1.W))
    val dout = Output(UInt(1.W))
    val serIn = Input(UInt(1.W))
    val paraOut = Output(UInt(4.W))
    val d = Input(UInt(4.W))
    val load = Input(Bool())
    val serOut = Output(UInt(1.W))
  })

  val din = io.din

  //- start shift_register
  val shiftReg = Reg(UInt(4.W))
  shiftReg := Cat(shiftReg(2, 0), din)
  val dout = shiftReg(3)
  //- end

  // printf("shiftReg = %x %d %d\n", shiftReg, din, dout)

  val serIn = io.serIn

  //- start shift_paraout
  val outReg = RegInit(0.U(4.W))
  outReg := Cat(serIn, outReg(3, 1))
  val q = outReg
  //- end

  val d = io.d
  val load = io.load
  //- start shift_paraload
  val loadReg = RegInit(0.U(4.W))
  when (load) {
    loadReg := d
  } otherwise  {
    loadReg := Cat(0.U, loadReg(3, 1))
  }
  val serOut = loadReg(0)
  //- end

  io.serOut := serOut

  io.paraOut := q
  io.dout := dout
}
