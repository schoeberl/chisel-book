package wildcat.pipeline

import chisel3._

/*
 * This file is part of the RISC-V processor Wildcat.
 *
 * This is the common top-level for different implementations.
 * Interface is to instruction memory and data memory.
 * All SPMs, caches, and IOs shall be in a SoC top level
 *
 * Author: Martin Schoeberl (martin@jopdesign.com)
 *
 */
//- start wildcat_top
abstract class Wildcat() extends Module {
  val io = IO(new Bundle {
    val imem = new InstrIO()
    val dmem = new MemIO()
  })
}
//- end