package leros

import chisel3._
import chisel3.util._
import leros.shared.Constants._

//- start leros_decode_bundle
class DecodeOut extends Bundle {
  val operand = UInt(32.W)
  val enaMask = UInt(4.W)
  val op = UInt()
  val off = SInt(10.W)
  val isRegOpd = Bool()
  val useDecOpd = Bool()
  val isStore = Bool()
  // ... and more fields
  //- end
  val isStoreInd = Bool()
  val isStoreIndB = Bool()
  val isStoreIndH = Bool()
  val isLoadInd = Bool()
  val isLoadIndB = Bool()
  val isLoadIndH = Bool()
  val isDataAccess = Bool()
  val isByteOff = Bool()
  val isHalfOff = Bool()
  val isLoadAddr = Bool()
  val exit = Bool()
}

//- start leros_decode_bundle_init
object DecodeOut {

  val MaskNone = "b0000".U
  val MaskAll = "b1111".U

  def default: DecodeOut = {
    val v = Wire(new DecodeOut)
    v.operand := 0.U
    v.enaMask := MaskNone
    v.op := nop.U
    v.off := 0.S
    v.isRegOpd := false.B
    v.useDecOpd := false.B
    v.isStore := false.B
    // ... and more fields
    //- end
    v.isStoreInd := false.B
    v.isStoreIndB := false.B
    v.isStoreIndH := false.B
    v.isLoadInd := false.B
    v.isLoadIndB := false.B
    v.isLoadIndH := false.B
    v.isDataAccess := false.B
    v.isByteOff := false.B
    v.isHalfOff := false.B
    v.isLoadAddr := false.B
    v.exit := false.B
    v
  }
}

//- start leros_decode_init
class Decode() extends Module {
  val io = IO(new Bundle {
    val din = Input(UInt(16.W))
    val dout = Output(new DecodeOut)
  })

  import DecodeOut._

  val d = DecodeOut.default
  //- end

  // Branch uses only 4 bits for decode
  val isBranch = WireDefault(false.B)
  /* this is broken, why?
  switch (io.din >> 4.U) {
    is (BR.U >> 4.U) { isBranch := true.B }
    is (BRZ.U >> 4.U) { isBranch := true.B }
    is (BRNZ.U >> 4.U) { isBranch := true.B }
    is (BRP.U >> 4.U) { isBranch := true.B }
    is (BRN.U >> 4.U) { isBranch := true.B }
  }
   */
  def mask(i: Int) = ((i >> 4) & 0x0f).asUInt

  val field = io.din(15, 12)
  when (field === mask(BR)) { isBranch := true.B }
  when (field === mask(BRZ)) { isBranch := true.B }
  when (field === mask(BRNZ)) { isBranch := true.B }
  when (field === mask(BRP)) { isBranch := true.B }
  when (field === mask(BRN)) { isBranch := true.B }

  val instr = Mux(isBranch, io.din & (BRANCH_MASK.U << 8), io.din)

  val noSext = WireDefault(false.B)
  val sigExt = Wire(SInt(32.W))
  sigExt := instr(7, 0).asSInt
  d.operand := sigExt.asUInt
  when (noSext) { d.operand := instr(7, 0) }

  //- start leros_decode
  switch(instr(15, 8)) {
    is(ADD.U) {
      d.op := add.U
      d.enaMask := MaskAll
      d.isRegOpd := true.B
    }
    is(ADDI.U) {
      d.op := add.U
      d.enaMask := MaskAll
      d.useDecOpd := true.B
    }
    is(SUB.U) {
      d.op := sub.U
      d.enaMask := MaskAll
      d.isRegOpd := true.B
    }
    is(SUBI.U) {
      d.op := sub.U
      d.enaMask := MaskAll
      d.useDecOpd := true.B
    }
    is(SHR.U) {
      d.op := shr.U
      d.enaMask := MaskAll
    }
    // ...
    //- end
    is(LD.U) {
      d.op := ld.U
      d.enaMask := MaskAll
      d.isRegOpd := true.B
    }
    is(LDI.U) {
      d.op := ld.U
      d.enaMask := MaskAll
      d.useDecOpd := true.B
    }
    is(AND.U) {
      d.op := and.U
      d.enaMask := MaskAll
      d.isRegOpd := true.B
    }
    is(ANDI.U) {
      d.op := and.U
      d.enaMask := MaskAll
      noSext := true.B
      d.useDecOpd := true.B
    }
    is(OR.U) {
      d.op := or.U
      d.enaMask := MaskAll
      d.isRegOpd := true.B
    }
    is(ORI.U) {
      d.op := or.U
      d.enaMask := MaskAll
      noSext := true.B
      d.useDecOpd := true.B
    }
    is(XOR.U) {
      d.op := xor.U
      d.enaMask := MaskAll
      d.isRegOpd := true.B
    }
    is(XORI.U) {
      d.op := xor.U
      d.enaMask := MaskAll
      noSext := true.B
      d.useDecOpd := true.B
    }
    is(LDHI.U) {
      d.op := ld.U
      d.enaMask := "b1110".U
      d.operand := sigExt(23, 0).asUInt ## 0.U(8.W)
      d.useDecOpd := true.B
    }
    // Following only useful for 32-bit Leros
    is(LDH2I.U) {
      d.op := ld.U
      d.enaMask := "b1100".U
      d.operand := sigExt(15, 0).asUInt ## 0.U(16.W)
      d.useDecOpd := true.B
    }
    is(LDH3I.U) {
      d.op := ld.U
      d.enaMask := "b1000".U
      d.operand := instr(7, 0) ## 0.U(24.W)
      d.useDecOpd := true.B
    }
    is (ST.U) {
      d.isStore := true.B
    }
    is (LDADDR.U) {
      d.isLoadAddr := true.B
    }
    is (LDIND.U) {
      d.isDataAccess := true.B
      d.isLoadInd := true.B
      d.op := ld.U
      d.enaMask := MaskAll
    }
    is (LDINDB.U) {
      d.isDataAccess := true.B
      d.isLoadIndB := true.B
      d.isByteOff := true.B
      d.op := ld.U
      d.enaMask := MaskAll
    }
    // TODO halfword
    is (STIND.U) {
      d.isDataAccess := true.B
      d.isStoreInd := true.B
    }
    is (STINDB.U) {
      d.isDataAccess := true.B
      d.isStoreIndB := true.B
      d.isByteOff := true.B
    }
    // TODO halfword
    is(SCALL.U) {
      d.exit := true.B
    }
  }

  val instrSignExt = Wire(SInt(32.W))
  instrSignExt := instr(7, 0).asSInt
  val off = Wire(SInt(10.W))
  off := instrSignExt << 2 // default word
  when(d.isHalfOff) {
    off := instrSignExt << 1
  }.elsewhen(d.isByteOff) {
    off := instrSignExt
  }
  d.off := off

  io.dout := d
}
