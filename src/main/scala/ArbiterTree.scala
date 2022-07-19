import chisel3._
import chisel3.util._

// Only one will be ready, as we cannot take two values
// This would need a shadow register, a reasonable optimisation
// Without optimisation one channel can only take one data every 2 clock cycles


//- start fun_arbiter_head
class ArbiterTree[T <: Data: Manifest](n: Int, private val gen: T) extends Module {
  val io = IO(new Bundle {
    val in = Flipped(Vec(n, new DecoupledIO(gen)))
    val out = new DecoupledIO(gen)
  })
  //- end

  //- start fun_arbiter
  def arbitrateTwo(a: DecoupledIO[T], b: DecoupledIO[T]) = {

    val idleA :: idleB :: hasA :: hasB :: Nil = Enum(4)
    val regData = Reg(gen)
    val regState = RegInit(idleA)
    val out = Wire(new DecoupledIO(gen))

    a.ready := regState === idleA
    b.ready := regState === idleB
    out.valid := (regState === hasA || regState === hasB)

    switch(regState) {
      is (idleA) {
        when (a.valid) {
          regData := a.bits
          regState := hasA
        } otherwise {
          regState := idleB
        }
      }
      is (idleB) {
        when (b.valid) {
          regData := b.bits
          regState := hasB
        } otherwise {
          regState := idleA
        }
      }
      is (hasA) {
        when (out.ready) {
          regState := idleB
        }
      }
      is (hasB) {
        when (out.ready) {
          regState := idleA
        }
      }
    }
    out.bits := regData
    out
  }
  //- end

  //- start fun_arbiter_end
  io.out <> io.in.reduceTree((a, b) => arbitrateTwo(a, b))
}
//- end