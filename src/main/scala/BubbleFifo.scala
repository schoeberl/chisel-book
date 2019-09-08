/*
 * Copyright: 2014, Technical University of Denmark, DTU Compute
 * Author: Martin Schoeberl (martin@jopdesign.com)
 * License: Simplified BSD License
 * 
 * Play with FIFO buffers.
 *
 * This code is a copy from the chisel-examples repo for easier
 * inclusion in the Chisel book.
 *
 */


import chisel3._
import chisel3.util._

/*
 * On signal naming:
 * 在信号的命名上：
 * 
 * Alter's FIFO component:
 * Altera的FIFO组成：
 * 
 * data - data in, q - data out, wrreq and rdreq
 * state: full and empty
 * 
 * Xilinx's FIFO component:
 * din and dout, wr_en, rd_en
 * state: full and empty
 * 
 */

//- start fifo_writer_io
//- 开始 FIFO写入端口
class WriterIO(size: Int) extends Bundle {
  val write = Input(Bool())
  val full = Output(Bool())
  val din = Input(UInt(size.W))
}
//- end
//- 结束

//- start fifo_reader_io
//- 开始 fifo读出端口
class ReaderIO(size: Int) extends Bundle {
  val read = Input(Bool())
  val empty = Output(Bool())
  val dout = Output(UInt(size.W))
}
//- end
//- 结束

/**
 * A single register (=stage) to build the FIFO.
 * 单寄存器来搭建FIFO
 */
//- start fifo_register
//- 开始 fifo寄存器
class FifoRegister(size: Int) extends Module {
  val io = IO(new Bundle {
    val enq = new WriterIO(size)
    val deq = new ReaderIO(size)
  })

  val empty :: full :: Nil = Enum(2)
  val stateReg = RegInit(empty)
  val dataReg = RegInit(0.U(size.W))

  when(stateReg === empty) {
    when(io.enq.write) {
      stateReg := full
      dataReg := io.enq.din
    }
  }.elsewhen(stateReg === full) {
    when(io.deq.read) {
      stateReg := empty
      // just to better see empty slots in the waveform 
      // 只是为了更方便查看波形图的空槽
      dataReg := 0.U 
    }
  }.otherwise {
    // There should not be an otherwise state
    // 不应该有其它状态
  }

  io.enq.full := (stateReg === full)
  io.deq.empty := (stateReg === empty)
  io.deq.dout := dataReg
}
//- end
//- 结束

/**
 * This is a bubble FIFO.
 * 这是一个冒泡FIFO
 */
//- start fifo
//- 开始FIFO
class BubbleFifo(size: Int, depth: Int) extends Module {
  val io = IO(new Bundle {
    val enq = new WriterIO(size)
    val deq = new ReaderIO(size)
  })

  val buffers = Array.fill(depth) { Module(new FifoRegister(size)) }
  for (i <- 0 until depth - 1) {
    buffers(i + 1).io.enq.din := buffers(i).io.deq.dout
    buffers(i + 1).io.enq.write := ~buffers(i).io.deq.empty
    buffers(i).io.deq.read := ~buffers(i + 1).io.enq.full
  }
  io.enq <> buffers(0).io.enq
  io.deq <> buffers(depth - 1).io.deq
}
//- end
//- 结束