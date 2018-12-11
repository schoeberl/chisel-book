package leros

import chisel3._
import chisel3.iotesters.PeekPokeTester
import chisel3.util._
import leros.Types._

//- start leros_types
object Types {
  val nop :: add :: sub :: and :: or :: xor :: ld :: shr :: Nil = Enum(8)
}
//- end

//- start leros_alu
class Alu(size: Int) extends Module {
  val io = IO(new Bundle {
    val op = Input(UInt(3.W))
    val a = Input(SInt(size.W))
    val b = Input(SInt(size.W))
    val y = Output(SInt(size.W))
  })

  val op = io.op
  val a = io.a
  val b = io.b
  val res = WireInit(0.S(size.W))

  switch(op) {
    is(add) {
      res := a + b
    }
    is(sub) {
      res := a - b
    }
    is(and) {
      res := a & b
    }
    is(or) {
      res := a | b
    }
    is(xor) {
      res := a ^ b
    }
    is (shr) {
      // the following does NOT result in an unsigned shift
      // res := (a.asUInt >> 1).asSInt
      // work around
      res := (a >> 1) & 0x7fffffff.S
    }
    is(ld) {
      res := b
    }
  }

  io.y := res
}
//- end

class AluTester(dut: Alu) extends PeekPokeTester(dut) {

  // TODO: this is not the best way look at functions defined as Enum.
  // Workaround would be defining constants

  //- start leros_alu_ref
  def alu(a: Int, b: Int, op: Int): Int = {

    op match {
      case 1 => a + b
      case 2 => a - b
      case 3 => a & b
      case 4 => a | b
      case 5 => a ^ b
      case 6 => b
      case 7 => a >>> 1
      case _ => -123 // This shall not happen
    }
  }
  //- end

  //- start leros_alu_testvec
  // Some interesting corner cases
  val interesting = Array(1, 2, 4, 123, 0, -1, -2, 0x80000000, 0x7fffffff)
  //- end

  //- start leros_alu_test
  def test(values: Seq[Int]) = {
    for (fun <- add to shr) {
      for (a <- values) {
        for (b <- values) {
          poke(dut.io.op, fun)
          poke(dut.io.a, a)
          poke(dut.io.b, b)
          step(1)
          expect(dut.io.y, alu(a, b, fun.toInt))
        }
      }
    }
  }
  //- end

  //- start leros_alu_testrun
  test(interesting)
  //- end

  //- start leros_alu_rand
  val randArgs = Seq.fill(100)(scala.util.Random.nextInt)
  test(randArgs)
  //- end

}

object AluTester extends App {
  iotesters.Driver(() => new Alu(32)) {
    c => new AluTester(c)
  }
}