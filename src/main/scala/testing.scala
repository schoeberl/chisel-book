//- start test_import
import chisel3._
import chisel3.iotesters._
//- end
//- 结束

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
//- 结束

//- start test_bench_simple
class TesterSimple(dut: DeviceUnderTest) extends PeekPokeTester(dut) {

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
//- 结束

//- start test_main_simple
object TesterSimple extends App {
  chisel3.iotesters.Driver(() => new DeviceUnderTest()) { c =>
    new TesterSimple(c)
  }
}
//- end
//- 结束

//- start test_bench
//- 开始测试平台
class Tester(dut: DeviceUnderTest) extends PeekPokeTester(dut) {

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
//- 结束

//- start test_main
//- 开始测试函数
object Tester extends App {
  chisel3.iotesters.Driver(() => new DeviceUnderTest()) { c =>
    new Tester(c)
  }
}
//- end
//- 结束

