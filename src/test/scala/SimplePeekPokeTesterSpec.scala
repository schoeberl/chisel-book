import chisel3.iotesters._
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

//- start scalatest_simple
class SimplePeekPokeTesterSpec extends AnyFlatSpec with Matchers {
  "Tester" should "pass" in {
    chisel3.iotesters.Driver(() => new DeviceUnderTest()) { c =>
      new TesterPeekPoke(c)
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

class SimplePeekPokeTesterPrintfSpec extends AnyFlatSpec with Matchers {

  "TesterPrinter" should "pass" in {
    chisel3.iotesters.Driver(() => new DeviceUnderTestPrintf()) { c =>
      new SimplePeekPokeTesterPrintf(c)
    } should be (true)
  }
}

