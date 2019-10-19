package leros

import chisel3._
import chisel3.iotesters.PeekPokeTester
import chisel3.util._
import Types._
import leros.shared.Constants._

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
  val res = WireDefault(0.S(size.W))

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

//- start leros_constants
package leros.shared {

object Constants {
  val NOP = 0x00
  val ADD = 0x08
  val ADDI = 0x09
  val SUB = 0x0c
  val SUBI = 0x0d
  val SHR = 0x10
  val LD = 0x20
  val LDI = 0x21
  val AND = 0x22
  val ANDI = 0x23
  val OR = 0x24
  val ORI = 0x25
  val XOR = 0x26
  val XORI = 0x27
  val LDHI = 0x29
  val LDH2I = 0x2a
  val LDH3I = 0x2b
  val ST = 0x30
  // ...
  //- end
  val OUT = 0x39 // is IN/OUT immediate only?
  val IN = 0x05
  val JAL = 0x40
  val LDADDR = 0x50
  val LDIND = 0x60
  val LDINDBU = 0x61
  val STIND = 0x70
  val STINDB = 0x71
  val BR = 0x80
  val BRZ = 0x90
  val BRNZ = 0xa0
  val BRP = 0xb0
  val BRN = 0xc0
  val SCALL = 0xff // 0 is simulator exit
}

} // end package

//- start leros_decode_bundle
class DecodeOut extends Bundle {
  val ena = Bool()
  val func = UInt()
  val exit = Bool()
}
//- end

//- start leros_decode_init
class Decode() extends Module {
  val io = IO(new Bundle {
    val din = Input(UInt(8.W))
    val dout = Output(new DecodeOut)
  })

  val f = WireDefault(nop)
  val imm = WireDefault(false.B)
  val ena = WireDefault(false.B)

  io.dout.exit := false.B
  //- end

  //- start leros_decode
  switch(io.din) {
    is(ADD.U) {
      f := add
      ena := true.B
    }
    is(ADDI.U) {
      f := add
      imm := true.B
      ena := true.B
    }
    is(SUB.U) {
      f := sub
      ena := true.B
    }
    is(SUBI.U) {
      f := sub
      imm := true.B
      ena := true.B
    }
    is(SHR.U) {
      f := shr
      ena := true.B
    }
    // ...
    //- end
    is(LD.U) {
      f := ld
      ena := true.B
    }
    is(LDI.U) {
      f := ld
      imm := true.B
      ena := true.B
    }
    is(AND.U) {
      f := and
      ena := true.B
    }
    is(ANDI.U) {
      f := and
      imm := true.B
      ena := true.B
    }
    is(OR.U) {
      f := or
      ena := true.B
    }
    is(ORI.U) {
      f := or
      imm := true.B
      ena := true.B
    }
    is(XOR.U) {
      f := xor
      ena := true.B
    }
    is(XORI.U) {
      f := xor
      imm := true.B
      ena := true.B
    }
    is(LDHI.U) {
      f := sub
      imm := true.B
      ena := true.B
    }
    // Following only useful for 32-bit Leros
    is(LDH2I.U) {
      f := sub
      imm := true.B
      ena := true.B
    }
    is(LDH3I.U) {
      f := sub
      imm := true.B
      ena := true.B
    }
    is(SCALL.U) {
      io.dout.exit := true.B
    }
  }
  io.dout.ena := ena
  io.dout.func := f
}

package leros.util {


import leros.shared.Constants._
import scala.io.Source

object Assembler {

  //- start leros_asm_hard
  val prog = Array[Int](
    0x0903, // addi 0x3
    0x09ff, // -1
    0x0d02, // subi 2
    0x21ab, // ldi 0xab
    0x230f, // and 0x0f
    0x25c3, // or 0xc3
    0x0000
  )

  def getProgramFix() = prog
  //- end

  //- start leros_asm_call
  def getProgram(prog: String) = {
    assemble(prog)
  }

  // collect destination addresses in first pass
  val symbols = collection.mutable.Map[String, Int]()

  def assemble(prog: String): Array[Int] = {
    assemble(prog, false)
    assemble(prog, true)
  }
  //- end

  //- start leros_asm_start
  def assemble(prog: String, pass2: Boolean): Array[Int] = {

    val source = Source.fromFile(prog)
    var program = List[Int]()
    var pc = 0

    def toInt(s: String): Int = {
      if (s.startsWith("0x")) {
        Integer.parseInt(s.substring(2), 16)
      } else {
        Integer.parseInt(s)
      }
    }

    def regNumber(s: String): Int = {
      assert(s.startsWith("r"), "Register numbers shall start with \'r\'")
      s.substring(1).toInt
    }
    //- end

    //- start leros_asm_match
    for (line <- source.getLines()) {
      if (!pass2) println(line)
      val tokens = line.trim.split(" ")
      val Pattern = "(.*:)".r
      val instr = tokens(0) match {
        case "//" => // comment
        case Pattern(l) => if (!pass2) symbols += (l.substring(0, l.length - 1) -> pc)
        case "add" => (ADD << 8) + regNumber(tokens(1))
        case "sub" => (SUB << 8) + regNumber(tokens(1))
        case "and" => (AND << 8) + regNumber(tokens(1))
        case "or" => (OR << 8) + regNumber(tokens(1))
        case "xor" => (XOR << 8) + regNumber(tokens(1))
        case "load" => (LD << 8) + regNumber(tokens(1))
        case "addi" => (ADDI << 8) + toInt(tokens(1))
        case "subi" => (SUBI << 8) + toInt(tokens(1))
        case "andi" => (ANDI << 8) + toInt(tokens(1))
        case "ori" => (ORI << 8) + toInt(tokens(1))
        case "xori" => (XORI << 8) + toInt(tokens(1))
        case "shr" => (SHR << 8)
        // ...
        case "" => // println("Empty line")
        case t: String => throw new Exception("Assembler error: unknown instruction: " + t)
        case _ => throw new Exception("Assembler error")
        //- end
        case "loadi" => (LDI << 8) + toInt(tokens(1))
        case "loadhi" => (LDHI << 8) + toInt(tokens(1))
        case "loadh2i" => (LDH2I << 8) + toInt(tokens(1))
        case "loadh3i" => (LDH3I << 8) + toInt(tokens(1))
        case "store" => (ST << 8) + regNumber(tokens(1))
        case "ldaddr" => (LDADDR << 8)
        case "ldind" => (LDIND << 8) + toInt(tokens(1))
        case "ldindbu" => (LDINDBU << 8) + toInt(tokens(1))
        case "stind" => (STIND << 8) + toInt(tokens(1))
        case "stindb" => (STINDB << 8) + toInt(tokens(1))
        case "br" => (BR << 8) + (if (pass2) symbols(tokens(1)) else 0)
        case "brz" => (BRZ << 8) + (if (pass2) symbols(tokens(1)) else 0)
        case "brnz" => (BRNZ << 8) + (if (pass2) symbols(tokens(1)) else 0)
        case "brp" => (BRP << 8) + (if (pass2) symbols(tokens(1)) else 0)
        case "brn" => (BRN << 8) + (if (pass2) symbols(tokens(1)) else 0)
        case "in" => (IN << 8) + toInt(tokens(1))
        case "out" => (OUT << 8) + toInt(tokens(1))
        case "scall" => (SCALL << 8) + toInt(tokens(1))
      }

      instr match {
        case (a: Int) => {
          program = a :: program
          pc += 1
        }
        case _ => // println("Something else")
      }
    }
    val finalProg = program.reverse.toArray
    if (!pass2) {
      println(s"The program:")
      // finalProg.foreach(printf("0x%02x ", _))
      println()
    }
    finalProg
  }

}
}

