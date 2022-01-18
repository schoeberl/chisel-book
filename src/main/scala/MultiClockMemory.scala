import chisel3._
import chisel3.util._

//- start multi_clock_memory
class MemoryIO(val n: Int, val w: Int) extends Bundle {
  val clk = Input(Bool())
  val addr  = Input(UInt(log2Up(n).W))
  val datai = Input(UInt(w.W))
  val datao = Output(UInt(w.W))
  val en = Input(Bool())
  val we = Input(Bool())
}

class MultiClockMemory(ports: Int, n: Int = 1024, w: Int = 32) extends Module {
  val io = IO(new Bundle {
    val ps = Vec(ports, new MemoryIO(n, w))
  })

  val ram = SyncReadMem(n, UInt(w.W))

  for (i <- 0 until ports) {
    val p = io.ps(i)
    withClock(p.clk.asClock) {
      val datao = WireDefault(0.U(w.W))
      when(p.en) {
        datao := ram(p.addr)
        when(p.we) {
          ram(p.addr) := p.datai
        }
      }
      p.datao := datao
    }
  }
}
//- end

object MultiClockMemory extends App {
  emitVerilog(new MultiClockMemory(3), Array("--target-dir", "generated"))
}
