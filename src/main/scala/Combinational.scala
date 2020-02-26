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
  val e = (a & b) | c
  //- end

  //- start comb_use
  val f = ~e
  //- end

  /* this is an error example
  //- start comb_error
  e := c & b
  //- end
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
  val w = Wire(UInt())

  w := 0.U
  when (cond) {
    w := 3.U
  }
  //- end
  io.out := w
}

class CombWhen2Untested extends Module {
  val io = IO(new Bundle {
    val data = Input(UInt(4.W))
    val out = Output(Bool())
  })

  val coinSum = io.data
  val price = 5.U
  //- start comb_wire2
  val enoughMoney = Wire(Bool())

  enoughMoney := false.B
  when (coinSum >= price) {
    enoughMoney := true.B
  }
  //- end
  io.out := enoughMoney
}

class CombOther extends Module {
  val io = IO(new Bundle {
    val cond = Input(Bool())
    val out = Output(UInt(4.W))
  })

  val cond = io.cond
  //- start comb_otherwise
  val w = Wire(UInt())

  when (cond) {
    w := 1.U
  } .otherwise {
    w := 2.U
  }
  //- end
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
  val w = Wire(UInt())

  when (cond) {
    w := 1.U
  } .elsewhen (cond2) {
    w := 2.U
  } .otherwise {
    w := 3.U
  }
  //- end
  io.out := w
}

class CombWireDefault extends Module {
  val io = IO(new Bundle {
    val cond = Input(Bool())
    val out = Output(UInt(4.W))
  })

  val cond = io.cond
  //- start comb_wiredefault
  val w = WireDefault(0.U)

  when (cond) {
    w := 3.U
  }
  // ... and some more complex conditional assignments
  //- end
  io.out := w
}