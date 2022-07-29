import chisel3._

import chisel3.util._

class Arbiter3 extends Module {
  val io = IO(new Bundle{
    val request = Input(UInt(3.W))
    val grant = Output(UInt(3.W))
  })

  val request = VecInit(io.request.asBools)

  //- start arbiter3
  val grant = VecInit(false.B, false.B, false.B)
  val notGranted = VecInit(false.B, false.B)

  grant(0) := request(0)
  notGranted(0) := !grant(0)
  grant(1) := request(1) && notGranted(0)
  notGranted(1) := !grant(1) && notGranted(0)
  grant(2) := request(2) && notGranted(1)
  //- end

  io.grant := grant.asUInt
}

class Arbiter3Direct extends Module {
  val io = IO(new Bundle{
    val request = Input(UInt(3.W))
    val grant = Output(UInt(3.W))
  })

  val request = io.request

  //- start arbiter3-direct
  val grant = WireDefault("b0000".U(3.W))
  switch (request) {
    is ("b000".U) { grant := "b000".U}
    is ("b001".U) { grant := "b001".U}
    is ("b010".U) { grant := "b010".U}
    is ("b011".U) { grant := "b001".U}
    is ("b100".U) { grant := "b100".U}
    is ("b101".U) { grant := "b001".U}
    is ("b110".U) { grant := "b010".U}
    is ("b111".U) { grant := "b001".U}
  }
  //- end
  io.grant := grant
}

class Arbiter3Loop extends Module {
  val io = IO(new Bundle{
    val request = Input(UInt(3.W))
    val grant = Output(UInt(3.W))
  })

  val request = VecInit(io.request.asBools)
  val n = request.length

  //- start arbiter3-loop
  val grant = VecInit.fill(n)(false.B)
  val notGranted = VecInit.fill(n)(false.B)

  grant(0) := request(0)
  notGranted(0) := !grant(0)
  for (i <- 1 until n) {
    grant(i) := request(i) && notGranted(i-1)
    notGranted(i) := !grant(i) && notGranted(i-1)
  }
  //- end

  io.grant := grant.asUInt
}

/*
This does not work with plain UInts, as Chisel has partial assignment issues

  val grant = WireDefault(0.U(3.W))

  grant(0) := request(0)
  notGranted(0) := ~grant(0)
  grant(1) := request(1) & notGranted(0)
  notGranted(1) := ~grant(1) & notGranted(0)
  grant(2) := request(2) & notGranted(1)

 */
