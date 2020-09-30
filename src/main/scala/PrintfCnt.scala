import chisel3._

class PrintfCnt extends Module {
  val io = IO(new Bundle() {
    val out = Output(UInt())
  })

  val cnt = RegInit(0.U(4.W))
  cnt := cnt + 1.U
  // Note: in ChiselTest we see the reset value, not in PeekPokeTester
  printf("printf: %d\n", cnt)

  io.out := cnt
}
