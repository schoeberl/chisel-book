import Chisel._

/**
 * Test the counter by printing out the value at each clock cycle.
 */
class RegisterTester(c: Register) extends Tester(c) {

  peek(c.io.out)
  poke(c.io.in, 13)
  step(1)
  expect(c.io.out, 13)
}

/**
 * Create a counter and a tester.
 */
object RegisterTester {
  def main(args: Array[String]): Unit = {
    chiselMainTest(Array("--genHarness", "--test", "--backend", "c",
      "--compile", "--targetDir", "generated"),
      () => Module(new Register())) {
        c => new RegisterTester(c)
      }
  }
}
