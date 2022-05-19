import chisel3._

//- start encdec_util
import chisel3.util._
//- end

class EncDec extends Module {
  val io = IO(new Bundle {
    val decin = Input(UInt(2.W))
    val decout = Output(UInt(4.W))
    val encin = Input(UInt(4.W))
    val encout = Output(UInt(2.W))
    val largeEncIn = Input(UInt(16.W))
    val largeEncOut = Output(UInt(4.W))
  })

  val sel = io.decin

  val result = Wire(UInt(4.W))

  //- start encdec_dec
  result := 0.U

  switch(sel) {
    is (0.U) { result := 1.U}
    is (1.U) { result := 2.U}
    is (2.U) { result := 4.U}
    is (3.U) { result := 8.U}
  }
  //- end

  //- start encdec_decbin
  switch (sel) {
    is ("b00".U) { result := "b0001".U}
    is ("b01".U) { result := "b0010".U}
    is ("b10".U) { result := "b0100".U}
    is ("b11".U) { result := "b1000".U}
  }
  //- end

  //- start encdec_shift
  result := 1.U << sel
  //- end

  io.decout := result

  val a = io.encin
  val b = Wire(UInt(2.W))
  //- start encdec_enc
  b := "b00".U
  switch (a) {
    is ("b0001".U) { b := "b00".U}
    is ("b0010".U) { b := "b01".U}
    is ("b0100".U) { b := "b10".U}
    is ("b1000".U) { b := "b11".U}
  }
  //- end

  io.encout := b

  val hotIn = io.largeEncIn

  //- start encdec_large
  val v = Wire(Vec(16, UInt(4.W)))
  v(0) := 0.U
  for (i <- 1 until 16) {
    v(i) := Mux(hotIn(i), i.U, 0.U) | v(i - 1)
  }
  val encOut = v(15)
  //- end
  io.largeEncOut := encOut
}


