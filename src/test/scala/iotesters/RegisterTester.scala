import chisel3._
import chisel3.iotesters._

class RegisterTester(c: Registers) extends PeekPokeTester(c) {
  
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
    chisel3.iotesters.Driver(() => new Registers()) { c =>
      new RegisterTester(c) }
  }
}
