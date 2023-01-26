package leros.shared

//- start leros_constants
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
  val LDINDB = 0x61
  val STIND = 0x70
  val STINDB = 0x71
  val BR = 0x80
  val BRZ = 0x90
  val BRNZ = 0xa0
  val BRP = 0xb0
  val BRN = 0xc0
  val SCALL = 0xff // 0 is simulator exit

  val BRANCH_MASK = 0xf0

  //- start leros_types
  // Alu ops
  val nop = 0
  val add = 1
  val sub = 2
  val and = 3
  val or = 4
  val xor = 5
  val ld = 6
  val shr = 7
  //- end
} // end package
