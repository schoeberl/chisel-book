import chisel3._

class Combinational extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(4.W))
    val b = Input(UInt(4.W))
    val c = Input(UInt(4.W))
    val out = Output(UInt(4.W))
    val out2 = Output(UInt(4.W))
  })

  val a = io.a
  val b = io.b
  val c = io.c

  //- start comb_bool
  //- 开始逻辑符号的组合逻辑
  val e = (a & b) | c
  //- end
  //- 结束

  //- start comb_use
  //- 开始使用组合逻辑
  val f = ~e
  //- end
  //- 结束

  /* this is an error example
  /* 这是一个错误的例子
  //- start comb_error
  //- 开始错误的组合逻辑（因为c没有被val定义）
  e := c & b
  //- end
  //- 结束
  */


  io.out := e
  io.out2 := f
}

class CombWhen extends Module {
  val io = IO(new Bundle {
    val cond = Input(Bool())
    val out = Output(UInt(4.W))
  })

  val cond = io.cond
  //- start comb_wire
  //- 开始线的组合逻辑
  val w = Wire(UInt())

  w := 0.U
  when (cond) {
    w := 3.U
  }
  //- end
  //- 结束
  io.out := w
}

class CombOther extends Module {
  val io = IO(new Bundle {
    val cond = Input(Bool())
    val out = Output(UInt(4.W))
  })

  val cond = io.cond
  //- start comb_otherwise
  //- 开始其余情况的组合逻辑
  val w = Wire(UInt())

  when (cond) {
    w := 1.U
  } otherwise {
    w := 2.U
  }
  //- end
  //- 结束
  io.out := w
}

class CombElseWhen extends Module {
  val io = IO(new Bundle {
    val cond = Input(Bool())
    val cond2 = Input(Bool())
    val out = Output(UInt(4.W))
  })

  val cond = io.cond
  val cond2 = io.cond2

  //- start comb_elsewhen
  //- 开始其余情况的组合逻辑
  val w = Wire(UInt())

  when (cond) {
    w := 1.U
  } .elsewhen (cond2) {
    w := 2.U
  } otherwise {
    w := 3.U
  }
  //- end
  //- 结束
  io.out := w
}

class CombWireDefault extends Module {
  val io = IO(new Bundle {
    val cond = Input(Bool())
    val out = Output(UInt(4.W))
  })

  // TODO: change to WireDefault when 3.2 is out
  val cond = io.cond
  //- start comb_wiredefault
  //- 开始默认线的组合逻辑
  val w = WireInit(0.U)

  when (cond) {
    w := 3.U
  }
  // ... and some more complex conditional assignments
  // ... 和一些更复杂的条件赋值
  //
  //- end
  //- 结束
  io.out := w
}