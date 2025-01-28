package wildcat.pipeline

import chisel3._
import chisel3.util._
import wildcat.AluFunct3._
import wildcat.AluType._
import wildcat.BranchFunct._
import wildcat.InstrType._
import wildcat.LoadStoreFunct._
import wildcat.Opcode._


object Functions {

  //- start wildcat_decode_func
  def decode(instruction: UInt) = {

    val opcode = instruction(6, 0)
    val func3 = instruction(14, 12)
    val decOut = Wire(new DecodedInstr())
    decOut.instrType := R.id.U
    decOut.isImm := false.B
    decOut.isLui := false.B
    decOut.isAuiPc := false.B
    decOut.isLoad := false.B
    decOut.isStore := false.B
    decOut.isBranch := false.B
    decOut.isJal := false.B
    decOut.isJalr := false.B
    decOut.rfWrite := false.B
    decOut.isECall := false.B
    decOut.isCssrw := false.B
    decOut.rs1Valid := false.B
    decOut.rs2Valid := false.B
    switch(opcode) {
      is(AluImm.U) {
        decOut.instrType := I.id.U
        decOut.isImm := true.B
        decOut.rfWrite := true.B
        decOut.rs1Valid := true.B
      }
      is(Alu.U) {
        decOut.instrType := R.id.U
        decOut.rfWrite := true.B
        decOut.rs1Valid := true.B
        decOut.rs2Valid := true.B
      }
      // and more cases
      //- end
      is(Branch.U) {
        decOut.instrType := SBT.id.U
        decOut.isImm := true.B
        decOut.isBranch := true.B
      }
      is(Load.U) {
        decOut.instrType := I.id.U
        decOut.rfWrite := true.B
        decOut.isLoad := true.B
      }
      is(Store.U) {
        decOut.instrType := S.id.U
        decOut.isStore := true.B
      }
      is(Lui.U) {
        decOut.instrType := U.id.U
        decOut.rfWrite := true.B
        decOut.isLui := true.B
      }
      is(AuiPc.U) {
        decOut.instrType := U.id.U
        decOut.rfWrite := true.B
        decOut.isAuiPc := true.B
      }
      is(Jal.U) {
        decOut.instrType := UJ.id.U
        decOut.rfWrite := true.B
        decOut.isJal := true.B
      }
      is(JalR.U) {
        decOut.instrType := I.id.U
        decOut.isImm := true.B
        decOut.rfWrite := true.B
        decOut.isJalr := true.B
      }
      is(System.U) {
        decOut.instrType := I.id.U
        when (func3 === 0.U) {
          decOut.isECall := true.B
        } .otherwise {
          decOut.isCssrw := true.B
        }
      }
    }
    decOut.aluOp := getAluOp(instruction)
    decOut.imm := getImm(instruction, decOut.instrType)
    decOut
  }

  def getAluOp(instruction: UInt): UInt = {

    val opcode = instruction(6, 0)
    val func3 = instruction(14, 12)
    val func7 = instruction(31, 25)

    val aluOp = WireDefault(ADD.id.U)
    switch(func3) {
      is(F3_ADD_SUB.U) {
        aluOp := ADD.id.U
        when(opcode =/= AluImm.U && opcode =/= JalR.U && func7 =/= 0.U) {
          aluOp := SUB.id.U
        }
      }
      is(F3_SLL.U) {
        aluOp := SLL.id.U
      }
      is(F3_SLT.U) {
        aluOp := SLT.id.U
      }
      is(F3_SLTU.U) {
        aluOp := SLTU.id.U
      }
      is(F3_XOR.U) {
        aluOp := XOR.id.U
      }
      is(F3_SRL_SRA.U) {
        when(func7 === 0.U) {
          aluOp := SRL.id.U
        }.otherwise {
          aluOp := SRA.id.U
        }
      }
      is(F3_OR.U) {
        aluOp := OR.id.U
      }
      is(F3_AND.U) {
        aluOp := AND.id.U
      }
    }
    aluOp
  }

  def compare(funct3: UInt, op1: UInt, op2: UInt): Bool = {
    val res = Wire(Bool())
    res := false.B
    switch(funct3) {
      is(BEQ.U) {
        res := op1 === op2
      }
      is(BNE.U) {
        res := op1 =/= op2
      }
      is(BLT.U) {
        res := op1.asSInt < op2.asSInt
      }
      is(BGE.U) {
        res := op1.asSInt >= op2.asSInt
      }
      is(BLTU.U) {
        res := op1 < op2
      }
      is(BGEU.U) {
        res := op1 >= op2
      }
    }
    res
  }

  def getImm(instruction: UInt, instrType: UInt): SInt = {

    val imm = Wire(SInt(32.W))
    imm := instruction(31, 20).asSInt
    switch(instrType) {
      is(I.id.U) {
        imm := (Fill(20, instruction(31)) ## instruction(31, 20)).asSInt
      }
      is(S.id.U) {
        imm := (Fill(20, instruction(31)) ## instruction(31, 25) ## instruction(11, 7)).asSInt
      }
      is(SBT.id.U) {
        imm := (Fill(19, instruction(31)) ## instruction(7) ## instruction(30, 25) ## instruction(11, 8) ## 0.U(1.W)).asSInt
      }
      is(U.id.U) {
        imm := (instruction(31, 12) ## Fill(12, 0.U)).asSInt
      }
      is(UJ.id.U) {
        imm := (Fill(11, instruction(31)) ## instruction(19, 12) ## instruction(20) ## instruction(30, 21) ## 0.U(1.W)).asSInt
      }
    }
    imm
  }

  // Input direct from instruction fetch, the synchronous memory contains the pipeline register
  def registerFile(rs1: UInt, rs2: UInt, rd: UInt, wrData: UInt, wrEna: Bool, useMem: Boolean = true) = {

    if (useMem) {
      val debugRegs = RegInit(VecInit(Seq.fill(32)(0.U(32.W)))) // only for debugging, not used in synthesis
      when(wrEna && rd =/= 0.U) {
        debugRegs(rd) := wrData
      }
      //- start wildcat_regfile
      val regs = SyncReadMem(32, UInt(32.W), SyncReadMem.WriteFirst)
      val rs1Val = regs.read(rs1)
      val rs2Val = regs.read(rs2)
      when(wrEna && rd =/= 0.U) {
        regs.write(rd, wrData)
      }
      (rs1Val, rs2Val, debugRegs)
      //- end
    } else {
      // non need for forwarding as read address is delayed
      val regs = RegInit(VecInit(Seq.fill(32)(0.U(32.W))))
      val rs1Val = regs(RegNext(rs1))
      val rs2Val = regs(RegNext(rs2))
      when(wrEna && rd =/= 0.U) {
        regs(rd) := wrData
      }
      (rs1Val, rs2Val, regs)
    }
  }

  // TODO: something missing? Looks OK now. Wait for the tests.
  //- start wildcat_alu
  def alu(op: UInt, a: UInt, b: UInt): UInt = {
    val res = Wire(UInt(32.W))
    res := DontCare
    switch(op) {
      is(ADD.id.U) {
        res := a + b
      }
      is(SUB.id.U) {
        res := a - b
      }
      is(AND.id.U) {
        res := a & b
      }
      is(OR.id.U) {
        res := a | b
      }
      is(XOR.id.U) {
        res := a ^ b
      }
      is(SLL.id.U) {
        res := a << b(4, 0)
      }
      is(SRL.id.U) {
        res := a >> b(4, 0)
      }
      is(SRA.id.U) {
        res := (a.asSInt >> b(4, 0)).asUInt
      }
      is(SLT.id.U) {
        res := (a.asSInt < b.asSInt).asUInt
      }
      is(SLTU.id.U) {
        res := (a < b).asUInt
      }
    }
    res
  }
  //- end

  def selectLoadData(data: UInt, func3: UInt, memLow: UInt): UInt = {
    val res = Wire(UInt(32.W))
    res := data
    switch(func3) {
      is(LB.U) {
        switch(memLow) {
          is(0.U) {
            res := Fill(24, data(7)) ## data(7, 0)
          }
          is(1.U) {
            res := Fill(24, data(15)) ## data(15, 8)
          }
          is(2.U) {
            res := Fill(24, data(23)) ## data(23, 16)

          }
          is(3.U) {
            res := Fill(24, data(31)) ## data(31, 24)
          }
        }
      }
      is(LH.U) {
        switch(memLow) {
          is(0.U) {
            res := Fill(16, data(15)) ## data(15, 0)
          }
          is(2.U) {
            res := Fill(16, data(31)) ## data(31, 16)
          }
        }
      }
      is(LBU.U) {
        switch(memLow) {
          is(0.U) {
            res := data(7, 0)
          }
          is(1.U) {
            res := data(15, 8)
          }
          is(2.U) {
            res := data(23, 16)
          }
          is(3.U) {
            res := data(31, 24)
          }
        }
      }
      is(LHU.U) {
        switch(memLow) {
          is(0.U) {
            res := data(15, 0)
          }
          is(2.U) {
            res := data(31, 16)
          }
        }
      }
    }
    res
  }

  def getWriteData(data: UInt, func3: UInt, memLow: UInt) = {
    val wrData = WireDefault(data)
    val wrEnable = VecInit(Seq.fill(4)(false.B))
    switch(func3) {
      is(SB.U) {
        wrData := data(7, 0) ## data(7, 0) ## data(7, 0) ## data(7, 0)
        wrEnable(memLow) := true.B
      }
      is(SH.U) {
        wrData := data(15, 0) ## data(15, 0)
        switch(memLow) {
          is(0.U) {
            wrEnable(0) := true.B
            wrEnable(1) := true.B
          }
          is(2.U) {
            wrEnable(2) := true.B
            wrEnable(3) := true.B
          }
        }
      }
      is(SW.U) {
        wrEnable := VecInit(Seq.fill(4)(true.B))
      }
    }
    (wrData, wrEnable)
  }
}
