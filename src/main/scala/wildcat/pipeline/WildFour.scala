package wildcat.pipeline

import chisel3._
import wildcat.Opcode._
import wildcat.pipeline.Functions._

/*
 * This file is part of the RISC-V processor Wildcat.
 *
 * This is a three stage pipeline.
 *
 * 0. PC generation
 * 1. Fetch
 * 2. Decode, register read
 * 3. Execute, memory access
 *
 * Author: Martin Schoeberl (martin@jopdesign.com)
 *
 */
class WildFour() extends Wildcat() {

  // some forward declarations
  val stall = WireDefault(false.B)
  val wbData = Wire(UInt(32.W))
  val wbDest = Wire(UInt(5.W))
  val wrEna = WireDefault(true.B)

  val writeBackData = Wire(UInt(32.W))
  val writeBackDest = Wire(UInt(5.W))
  val writeBackEna = Wire(Bool())

  val doBranch = WireDefault(false.B)
  val branchTarget = WireDefault(0.U)

  // Pipe and forwarding register from MEM
  val exMem = new Bundle() {
    val valid = Bool()
    val wbDest = UInt(5.W)
    val wbData = UInt(32.W)
    val address = UInt(32.W)
    val isLoad = Bool()
  }
  val exMemReg = RegInit(0.U.asTypeOf(exMem))
  // Forwarding register from WB
  val memFwd = new Bundle() {
    val valid = Bool()
    val wbDest = UInt(5.W)
    val wbData = UInt(32.W)
  }
  val memFwdReg = RegInit(0.U.asTypeOf(memFwd))

  // The ROM has a register that is reset to 0, therefore clock cycle 1 is the first instruction.
  // Needed if we want to start from a different address.
  // PC generation
//  val pcReg = RegInit(-4.S(32.W).asUInt)
  val pcReg = RegInit(0.S(32.W).asUInt) // keep it simpler for now for the waveform viewing

  val pcNext = Mux(doBranch, branchTarget, pcReg + 4.U)
  pcReg := pcNext
  io.imem.address := pcNext

  // Fetch
  val instr = io.imem.data

  // Decode and register read
  val pcRegReg = RegNext(pcReg)
  val instrReg = RegInit(0x00000033.U) // nop on reset
  instrReg := Mux(doBranch, 0x00000033.U, instr)
  val rs1 = instr(19, 15)
  val rs2 = instr(24, 20)
  val rd = instr(11, 7)
  val (rs1Val, rs2Val, debugRegs) = registerFile(rs1, rs2, writeBackDest, writeBackData, writeBackEna, false)

  val decOut = decode(instrReg)

  val decEx = Wire(new Bundle() {
    val decOut = new DecodedInstr()
    val valid = Bool()
    val pc = UInt(32.W)
    val rs1 = UInt(5.W)
    val rs2 = UInt(5.W)
    val rd = UInt(5.W)
    val rs1Val = UInt(32.W)
    val rs2Val = UInt(32.W)
    val func3 = UInt(3.W)
    val branchInstr = Bool()
    val isStore = Bool()
    val isLoad = Bool()
  })
  decEx.decOut := decOut
  decEx.valid := !doBranch
  decEx.pc := pcRegReg
  decEx.rs1 := instrReg(19, 15)
  decEx.rs2 := instrReg(24, 20)
  decEx.rd := instrReg(11, 7)
  decEx.rs1Val := rs1Val
  decEx.rs2Val := rs2Val
  decEx.func3 := instrReg(14, 12)
  decEx.branchInstr := instrReg(6, 0) === Branch.U
  decEx.isStore := decOut.isStore
  decEx.isLoad := decOut.isLoad

  // Execute
  val decExReg = RegInit(0.U.asTypeOf(decEx))
  decExReg := decEx

  // Forwarding
  val v1 = Mux(exMemReg.valid && exMemReg.wbDest === decExReg.rs1, exMemReg.wbData,
    Mux(memFwdReg.valid && memFwdReg.wbDest === decExReg.rs1, memFwdReg.wbData, decExReg.rs1Val))
  val v2 = Mux(exMemReg.valid && exMemReg.wbDest === decExReg.rs2, exMemReg.wbData,
    Mux(memFwdReg.valid && memFwdReg.wbDest === decExReg.rs2, memFwdReg.wbData, decExReg.rs2Val))

  val res = Wire(UInt(32.W))
  val val2 = Mux(decExReg.decOut.isImm, decExReg.decOut.imm.asUInt, v2)
  res := alu(decExReg.decOut.aluOp, v1, val2)
  when (decExReg.decOut.isLui) {
    res := decExReg.decOut.imm.asUInt
  }
  when (decExReg.decOut.isAuiPc) {
    res := (decExReg.pc.asSInt + decExReg.decOut.imm).asUInt
  }
  when (decExReg.decOut.isLoad) {
    res := io.dmem.rdData
  }

  wbDest := decExReg.rd
  wbData := res
  when (decExReg.decOut.isJal || decExReg.decOut.isJalr) {
    wbData := decExReg.pc + 4.U
  }
  // Branching
  branchTarget := (decExReg.pc.asSInt + decExReg.decOut.imm).asUInt
  when (decExReg.decOut.isJalr) {
    branchTarget := res
  }
  doBranch := ((compare(decExReg.func3, v1, v2) && decExReg.branchInstr) || decExReg.decOut.isJal || decExReg.decOut.isJalr) && decExReg.valid
  wrEna := decExReg.valid && decExReg.decOut.rfWrite

  // pipe register stage

  // *** register write back either from pipe register or from memory read ***
  // Mux is part of Mem, output reg is for forwarding to ALU and memory itself
  // note that we need a stall for load use bubble

  exMemReg.valid := wrEna && (wbDest =/= 0.U) // is this needed here or is this already set in wrEna?
  exMemReg.wbDest := wbDest
  exMemReg.wbData := wbData
  exMemReg.address := (decExReg.rs1Val.asSInt + decExReg.decOut.imm).asUInt
  exMemReg.isLoad := decExReg.isLoad

  // Memory access
  // Forwarding to memory -- needs to change from reg after mem

  // val address = Mux(wrEna && (wbDest =/= 0.U) && wbDest === decEx.rs1, wbData, rs1Val)
  // val data = Mux(wrEna && (wbDest =/= 0.U) && wbDest === decEx.rs2, wbData, rs2Val)
  val data = exMemReg.wbData // TODO local forwarding, maybe?
  // TODO: this is one clock cycle to late. It needs to be the combinational value from EX stage

  val memAddress = exMemReg.address
  io.dmem.rdAddress := memAddress
  io.dmem.wrAddress := memAddress
  io.dmem.wrData := data
  io.dmem.wrEnable := Mux(decExReg.isStore, 15.U, 0.U)

  // write back mux in memory stage
  writeBackData := Mux(exMemReg.isLoad, io.dmem.rdData, exMemReg.wbData)
  writeBackDest := exMemReg.wbDest
  writeBackEna := exMemReg.valid

  // Forwarding register values to ALU and memory?
  memFwdReg.valid := exMemReg.valid
  memFwdReg.wbDest := exMemReg.wbDest
  memFwdReg.wbData := exMemReg.wbData

  // Just to exit tests
  val stop = decExReg.decOut.isECall
}