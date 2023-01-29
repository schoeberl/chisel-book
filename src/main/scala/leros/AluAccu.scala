package leros

import chisel3._
import chisel3.util._
import leros.shared.Constants._

/**
 * Leros ALU including the accumulator register.
 *
 * @param size
 */
//- start leros_alu
class AluAccu(size: Int) extends Module {
  val io = IO(new Bundle {
    val op = Input(UInt(3.W))
    val din = Input(UInt(size.W))
    val enaMask = Input(UInt(4.W))
    val enaByte = Input(Bool())
    val enaHalf = Input(Bool())
    val off = Input(UInt(2.W))
    val accu = Output(UInt(size.W))
  })

  val accuReg = RegInit(0.U(size.W))

  val op = io.op
  val a = accuReg
  val b = io.din
  val res = WireDefault(a)

  switch(op) {
    is(nop.U) {
      res := a
    }
    is(add.U) {
      res := a + b
    }
    is(sub.U) {
      res := a - b
    }
    is(and.U) {
      res := a & b
    }
    is(or.U) {
      res := a | b
    }
    is(xor.U) {
      res := a ^ b
    }
    is(shr.U) {
      res := a >> 1
    }
    is(ld.U) {
      res := b
    }
  }

  val byte = WireDefault(res(7, 0))
  val half = WireDefault(res(15, 0))
  when(io.off === 1.U) {
    byte := res(15, 8)
  }.elsewhen(io.off === 2.U) {
    byte := res(23, 16)
    half := res(31, 16)
  }.elsewhen(io.off === 3.U) {
    byte := res(31, 24)
  }
  val signExt = Wire(SInt(32.W))
  when (io.enaByte) {
    signExt := byte.asSInt
  } .otherwise {
    signExt := half.asSInt
  }

  // Workaround for missing subword assignments
  val split = Wire(Vec(4, UInt(8.W)))
  for (i <- 0 until 4) {
    split(i) := Mux(io.enaMask(i), res(8 * i + 7, 8 * i), accuReg(8 * i + 7, 8 * i))
  }

  when((io.enaByte || io.enaHalf) & io.enaMask.andR) {
    accuReg := signExt.asUInt
  } .otherwise {
    accuReg := split.asUInt
  }

  io.accu := accuReg
}
//- end
