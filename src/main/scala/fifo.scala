
// to avoid name clashes
package fifo

import Chisel._

//- start fifo_abstract
abstract class Fifo[T <: Data](gen: T) extends Module {
  val io = new Bundle {
    val enq = Flipped(new DecoupledIO(gen))
    val deq = new DecoupledIO(gen)
  }
}
//- end

// Ok here to redefine when having it in it's own packet
package fifo_private {

  //- start fifo_decoupled
  class DecoupledIO[T <: Data](gen: T) {
    val ready = Input(Bool())
    val valid = Output(Bool())
    val bits  = Output(gen)
  }
  //- end
}
