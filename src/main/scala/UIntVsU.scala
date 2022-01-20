import chisel3._

class UIntVsU extends Module {
  val io = IO(new Bundle() {
    val values = Output(Vec(5, UInt(3.W)))
  })

  val v = io.values

  v(0) := 7.U
  v(1) := 7.asUInt
  val scalaInt = 7
  v(2) := scalaInt.U
  v(3) := scalaInt.asUInt
  val signed = WireDefault(-1.S(3.W))
  v(4) := signed.asUInt
}

object UIntVsU extends App {
  println(getVerilogString(new UIntVsU))
}