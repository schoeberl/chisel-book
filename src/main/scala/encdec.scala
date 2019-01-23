import chisel3._
import chisel3.iotesters.PeekPokeTester

//- start encdec_util
import chisel3.util._
//- end

class EncDec extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(8.W))
    val b = Input(UInt(8.W))
    val x = Output(UInt(8.W))
  })

  val sel = io.a(1, 0)

  val result = Wire(UInt(4.W))

  //- start encdec_enc
  result := 0.U

  switch(sel) {
    is (0.U) { result := 1.U}
    is (1.U) { result := 2.U}
    is (2.U) { result := 4.U}
    is (3.U) { result := 8.U}
  }
  //- end

  //- start encdec_encbin
  // TODO
  switch(sel) {
    is (0.U) { result := 1.U}
    is (1.U) { result := 2.U}
    is (2.U) { result := 4.U}
    is (3.U) { result := 8.U}
  }
  //- end

  io.x := result
}

class TestEncDec(dut: EncDec) extends PeekPokeTester(dut) {

  poke(dut.io.a, 3.U)
  poke(dut.io.b, 1.U)
  step(1)
  expect(dut.io.x, 1)
  poke(dut.io.a, 2.U)
  poke(dut.io.b, 0.U)
  step(1)
  expect(dut.io.x, 0)
}

object TestEncDec extends App {
  chisel3.iotesters.Driver(() => new EncDec()) { c =>
    new TestEncDec(c)
  }
}

