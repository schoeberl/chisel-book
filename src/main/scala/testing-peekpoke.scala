
//- start test_import_peek_poke
import chisel3._
import chisel3.iotesters._
//- end

//- start test_dut
class DeviceUnderTest extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(2.W))
    val b = Input(UInt(2.W))
    val out = Output(UInt(2.W))
  })

  io.out := io.a & io.b
}
//- end

//- start test_dut_printf
class DeviceUnderTestPrintf extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(2.W))
    val b = Input(UInt(2.W))
    val out = Output(UInt(2.W))
  })

  io.out := io.a & io.b
  printf("dut: %d %d %d\n", io.a, io.b, io.out)
}
//- end

// TODO: shall we keep the code below for describing iotesters?
// If so, we should still use ScalaTest to drive them, not an App

//- start test_bench_simple_peek_poke
class TesterSimplePeekPoke(dut: DeviceUnderTest) extends PeekPokeTester(dut) {

  poke(dut.io.a, 0.U)
  poke(dut.io.b, 1.U)
  step(1)
  println("Result is: " + peek(dut.io.out).toString)
  poke(dut.io.a, 3.U)
  poke(dut.io.b, 2.U)
  step(1)
  println("Result is: " + peek(dut.io.out).toString)
}
//- end

//- start test_main_simple_peek_poke
object TesterSimplePeekPoke extends App {
  chisel3.iotesters.Driver(() => new DeviceUnderTest()) { c =>
    new TesterSimplePeekPoke(c)
  }
}
//- end

//- start test_bench_peek_poke
class TesterPeekPoke(dut: DeviceUnderTest) extends PeekPokeTester(dut) {

  poke(dut.io.a, 3.U)
  poke(dut.io.b, 1.U)
  step(1)
  expect(dut.io.out, 1)
  poke(dut.io.a, 2.U)
  poke(dut.io.b, 0.U)
  step(1)
  expect(dut.io.out, 0)
}
//- end

//- start test_main_peek_poke
object TesterPeekPoke extends App {
  chisel3.iotesters.Driver(() => new DeviceUnderTest()) { c =>
    new TesterPeekPoke(c)
  }
}
//- end

