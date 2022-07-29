import chisel3._
import chisel3.util.Decoupled

//- start counter_device
class CounterDevice extends Module {
  val io = IO(new Bundle() {
    val addr = Input(UInt(2.W))
    val wr = Input(Bool())
    val rd = Input(Bool())
    val wrData = Input(UInt(32.W))
    val rdData = Output(UInt(32.W))
    val ack = Output(Bool())
  })

  val ackReg = RegInit(false.B)
  val addrReg = RegInit(0.U(2.W))
  val cntRegs = RegInit(VecInit(Seq.fill(4)(0.U(32.W))))

  ackReg := io.rd || io.wr
  when(io.rd) {
    addrReg := io.addr
  }
  io.rdData := cntRegs(addrReg)

  for (i <- 0 until 4) {
    cntRegs(i) := cntRegs(i) + 1.U
  }
  when (io.wr) {
    cntRegs(io.addr) := io.wrData
  }

  io.ack := ackReg
}
//- end


object CounterDevice extends App {
  emitVerilog(new CounterDevice())
}

class MemoryMappedIO extends Bundle {
  val addr = Input(UInt())
  val wr = Input(Bool())
  val rd = Input(Bool())
  val wrData = Input(UInt())
  val rdData = Output(UInt())
  val ack = Output(Bool())
}

// Mapping as in classic PC serial port
// 0: status (control): bit 0 transmit ready, bit 1 rx data availabel
// 1: txd and rxd
// Question: is address a byte address or a word address?
// Simplest is to using word addresses, it does not really care

class MemMappedRV[T <: Data](gen: T, block: Boolean = false) extends Module {
  val io = IO(new Bundle() {
    val mem = new MemoryMappedIO()
    val out = Decoupled(gen)
    val in = Flipped(Decoupled(gen))
  })

  val statusReg = RegInit(0.U(2.W))
  val ackReg = RegInit(false.B)
  val addrReg = RegInit(0.U(1.W))

  statusReg := io.in.valid ## io.out.ready

  // non-blocking for now
  ackReg := io.mem.rd || io.mem.wr
  io.mem.ack := ackReg

  io.mem.rdData := Mux(addrReg === 0.U, statusReg, io.in.bits)
  io.out.bits := io.mem.wrData


}