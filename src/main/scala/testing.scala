import chisel3._
import chisel3.iotesters._

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

//- start test_bench_simple
class TesterSimple(c: DeviceUnderTest) extends PeekPokeTester(c) {

  poke(c.io.a, 0.U)
  poke(c.io.b, 1.U)
  step(1)
  println(peek(c.io.out).toString)
  poke(c.io.a, 3.U)
  poke(c.io.b, 2.U)
  step(1)
  println(peek(c.io.out).toString)
}
//- end

//- start test_main_simple
object TesterSimple extends App {
  chisel3.iotesters.Driver(() => new DeviceUnderTest()) { c =>
    new Tester(c)
  }
}
//- end

//- start test_bench
class Tester(c: DeviceUnderTest) extends PeekPokeTester(c) {

  poke(c.io.a, 0.U)
  poke(c.io.b, 1.U)
  step(1)
  println(peek(c.io.out).toString)
  expect(c.io.out, 13)
}
//- end

//- start test_main
object Tester extends App {
  chisel3.iotesters.Driver(() => new DeviceUnderTest()) { c =>
    new Tester(c)
  }
}
//- end

