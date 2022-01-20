import chisel3._

//- start sync_reset
class SyncReset extends Module {
  val io = IO(new Bundle() {
    val value = Output(UInt())
  })

  val syncReset = RegNext(RegNext(reset))
  val cnt = Module(new WhenCounter(5))
  cnt.reset := syncReset

  io.value := cnt.io.cnt
}
//- end


object SyncReset extends App {
  println(getVerilogString(new SyncReset))
}
