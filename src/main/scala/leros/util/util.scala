package leros.util

import chisel3.assert
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
      }
      //- end

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
