import chisel3._
import chisel3.iotesters._

class LogicTester(c: Logic) extends PeekPokeTester(c) {

  // TODO: more should be tested
  poke(c.io.a, 1)
  poke(c.io.b, 0)
  poke(c.io.c, 1)
  step(1)
  expect(c.io.out, 1)
}

object LogicTester {
  def main(args: Array[String]): Unit = {
    chisel3.iotesters.Driver(() => new Logic()) { c =>
      new LogicTester(c) }
  }
}
