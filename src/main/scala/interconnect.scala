import chisel3._
import chisel3.util._
import fifo._

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

//- start mem_io_bundle
class MemoryMappedIO extends Bundle {
  val address = Input(UInt(4.W))
  val rd = Input(Bool())
  val wr = Input(Bool())
  val rdData = Output(UInt(32.W))
  val wrData = Input(UInt(32.W))
  val ack = Output(Bool())
}
//- end

// Mapping as in classic PC serial port
// 0: status (control): bit 0 transmit ready, bit 1 rx data available
// 1: txd and rxd
// Question: is address a byte address or a word address?
// Simplest is to using word addresses, it does not really care
// non-blocking for now

//- start mem_io_rv
class MemMappedRV[T <: Data](gen: T, block: Boolean = false) extends Module {
  val io = IO(new Bundle() {
    val mem = new MemoryMappedIO()
    val tx = Decoupled(gen)
    val rx = Flipped(Decoupled(gen))
  })

  val statusReg = RegInit(0.U(2.W))
  val ackReg = RegInit(false.B)
  val addrReg = RegInit(0.U(1.W))
  val rdDlyReg = RegInit(false.B)

  statusReg := io.rx.valid ## io.tx.ready

  // ack
  ackReg := io.mem.rd || io.mem.wr
  io.mem.ack := ackReg

  // read from status or rx
  when (io.mem.rd) {
    addrReg := io.mem.address
  }
  rdDlyReg := io.mem.rd
  io.rx.ready := false.B
  when (addrReg === 1.U && rdDlyReg) {
    io.rx.ready := true.B
  }
  io.mem.rdData := Mux(addrReg === 0.U, statusReg, io.rx.bits)

  // write to tx
  io.tx.bits := io.mem.wrData
  io.tx.valid := io.mem.wr
}
//- end

class UseMemMappedRV[T <: Data](gen: T) extends Module {
  val io = IO(new Bundle() {
    val mem = new MemoryMappedIO()
  })

  val memDevice = Module(new MemMappedRV(gen))
  val fifo = Module(new RegFifo(gen, 3))
  memDevice.io.tx <> fifo.io.enq
  memDevice.io.rx <> fifo.io.deq
  io.mem <> memDevice.io.mem
}
