import chisel3._

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

/*
This does not work with plain UInts, as Chisel has parital assignment issues

  val grant = WireDefault(0.U(3.W))

  grant(0) := request(0)
  grant(1) := request(1) & ~grant(0)
  grant(2) := request(2) & ~grant(1)
 */
