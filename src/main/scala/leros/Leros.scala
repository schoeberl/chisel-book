package leros

import chisel3._
import chisel3.util._

/**
 * Leros top level.
 *
 * Sequential implementation with two states.
 */
class Leros(size: Int, memAddrWidth: Int, prog: String, fmaxReg: Boolean) extends LerosBase(size, memAddrWidth, prog, fmaxReg) {

  //- start leros_states
  object State extends ChiselEnum {
    val fetch, execute = Value
  }
  import State._

  val stateReg = RegInit(fetch)

  switch(stateReg) {
    is(fetch) {
      stateReg := execute
    }
    is(execute) {
      stateReg := fetch
    }
  }
  //- end

  //- start leros_main_state
  val alu = Module(new AluAccu(size))
  val accu = alu.io.accu

  // The main architectural state
  val pcReg = RegInit(0.U(memAddrWidth.W))
  val addrReg = RegInit(0.U(memAddrWidth.W))

  val pcNext = WireDefault(pcReg + 1.U)
  //- end

  //- start leros_fetch
  val mem = Module(new InstrMem(memAddrWidth, prog))
  mem.io.addr := pcNext
  val instr = mem.io.instr
  //- end

  // Decode
  //- start leros_decode_inst
  val dec = Module(new Decode())
  dec.io.din := instr
  val decout = dec.io.dout

  val decReg = RegInit(DecodeOut.default)
  when (stateReg === fetch) {
    decReg := decout
  }
  //- end

  val effAddr = (addrReg.asSInt + decout.off).asUInt
  val effAddrWord = (effAddr >> 2).asUInt
  val effAddrOff = Wire(UInt(2.W))
  effAddrOff := effAddr & 0x03.U
  val vecAccu = Wire(Vec(4, UInt(8.W)))
  for (i <- 0 until 4) {
    vecAccu(i) := accu(i*8 + 7, i*8)
  }
  // printf("%x %x %x %x\n", effAddr, effAddrWord, effAddrOff, decout.off)

  // Data memory, including the register memory
  // read in feDec, write in exe
  //- start leros_dmem_inst
  val dataMem = Module(new DataMem((memAddrWidth)))

  val memAddr = Mux(decout.isDataAccess, effAddrWord, instr(7, 0))
  val memAddrReg = RegNext(memAddr)
  val effAddrOffReg = RegNext(effAddrOff)
  dataMem.io.rdAddr := memAddr
  val dataRead = dataMem.io.rdData
  dataMem.io.wrAddr := memAddrReg
  dataMem.io.wrData := accu
  dataMem.io.wr := false.B
  dataMem.io.wrMask := "b1111".U
  //- end

  // ALU connection
  alu.io.op := decReg.op
  alu.io.enaMask := 0.U
  alu.io.enaByte := decReg.isLoadIndB
  alu.io.off := effAddrOffReg
  // this should be a single signal from decode (what did I mean with this?)
  alu.io.din := Mux(decReg.useDecOpd, decReg.operand, dataRead)

  // connection to the external world (test)
  val exit = RegInit(false.B)
  val outReg = RegInit(0.U(32.W))
  io.dout := outReg

  switch(stateReg) {
    is (fetch) {
      // nothing here
    }

    is (execute) {
      pcReg := pcNext
      alu.io.enaMask := decReg.enaMask
      when (decReg.isLoadAddr) {
        addrReg := accu
        alu.io.enaMask := 0.U
      }
      when (decReg.isLoadInd) {
        // nothing to be done here
      }
      when(decReg.isLoadIndB) {
        // nothing to be done here
        // probably sign extend then
      }
      when (decReg.isStore) {
        dataMem.io.wr := true.B
        alu.io.enaMask := 0.U
      }
      when(decReg.isStoreInd) {
        dataMem.io.wr := true.B
        alu.io.enaMask := 0.U
      }
      when(decReg.isStoreIndB) {
        dataMem.io.wr := true.B
        dataMem.io.wrMask := "b0001".U << effAddrOffReg
        alu.io.enaMask := 0.U
        vecAccu(effAddrOffReg) := accu(7, 0)
        dataMem.io.wrData := vecAccu(3) ## vecAccu(2) ## vecAccu(1) ## vecAccu(0)
      }
    }

  }

  exit := RegNext(decReg.exit)

  if (fmaxReg) {
    io.dbg.acc := RegNext(RegNext((accu)))
    io.dbg.pc := RegNext(RegNext((pcReg)))
    io.dbg.instr := RegNext(RegNext((instr)))
    io.dbg.exit := RegNext(RegNext((exit)))
  } else {
    io.dbg.acc := accu
    io.dbg.pc := pcReg
    io.dbg.instr := instr
    io.dbg.exit := exit
  }
}

object Leros extends App {
  emitVerilog(new Leros(32, 10, args(0), true), Array("--target-dir", "generated"))
}