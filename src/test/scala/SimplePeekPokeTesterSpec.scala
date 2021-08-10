import chisel3.iotesters._
import org.scalatest._

//- start scalatest_simple
class SimplePeekPokeTesterSpec extends FlatSpec with Matchers {
  "Tester" should "pass" in {
    chisel3.iotesters.Driver(() => new DeviceUnderTest()) { c =>
      new Tester(c)
    } should be (true)
  }
}
//- end



class SimplePeekPokeTesterPrintf(dut: DeviceUnderTestPrintf) extends PeekPokeTester(dut) {

  for (a <- 0 until 4) {
    for (b <- 0 until 4) {
      poke(dut.io.a, a)
      poke(dut.io.b, b)
      step(1)
    }
  }
}

class SimplePeekPokeTesterPrintfSpec extends FlatSpec with Matchers {

  "TesterPrinter" should "pass" in {
    chisel3.iotesters.Driver(() => new DeviceUnderTestPrintf()) { c =>
      new SimplePeekPokeTesterPrintf(c)
    } should be (true)
  }
}

