package leros

import chisel3._
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
    val a = Input(UInt(size.W))
    val b = Input(UInt(size.W))
    val y = Output(UInt(size.W))
  })


  val op = io.op
  val a = io.a
  val b = io.b
  val res = WireInit(0.U(size.W))

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
    is(shr) {
      res := a >> 1
    }
    is(ld) {
      res := b
    }
  }

  io.y := res
}
//- end
