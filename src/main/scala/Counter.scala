import chisel3._

class Count100 extends Module {
  val io = IO(new Bundle {
    val cnt = Output(UInt(8.W))
  })

  //- start counter
  //- 开始计数器
  val cntReg = RegInit(0.U(8.W))

  cntReg := Mux(cntReg === 9.U, 0.U, cntReg + 1.U)
  //- end
  //- 结束

  io.cnt := cntReg
}

abstract class Counter(n: Int) extends Module {
  val io = IO(new Bundle{
    val cnt = Output(UInt(8.W))
    val tick = Output(Bool())
  })
}

class WhenCounter(n: Int) extends Counter(n) {

  val N = (n-1).U

  //- start when_counter
  //- 开始条件计数
  val cntReg = RegInit(0.U(8.W))

  cntReg := cntReg + 1.U
  when(cntReg === N) {
    cntReg := 0.U
  }
  //- end
  //- 结束

  io.tick := cntReg === N
  io.cnt := cntReg
}

class MuxCounter(n: Int) extends Counter(n) {

  val N = (n-1).U

  //- start mux_counter
  //- 开始复用器计数
  val cntReg = RegInit(0.U(8.W))

  cntReg := Mux(cntReg === N, 0.U, cntReg + 1.U)
  //- end
  //- 结束

  io.tick := cntReg === N
  io.cnt := cntReg
}

class DownCounter(n: Int) extends Counter(n) {

  val N = (n-1).U

  //- start down_counter
  //- 开始向下计数
  val cntReg = RegInit(N)

  cntReg := cntReg - 1.U
  when(cntReg === 0.U) {
    cntReg := N
  }
  //- end
  //- 结束

  io.tick := cntReg === N
  io.cnt := cntReg
}

class FunctionCounter(n: Int) extends Counter(n) {

  //- start function_counter
  //- 开始函数计数
  // This function returns a counter
  // 这个函数返回计数器
  def genCounter(n: Int) = {
    val cntReg = RegInit(0.U(8.W))
    cntReg := Mux(cntReg === n.U, 0.U, cntReg + 1.U)
    cntReg
  }

  // now we can easily create many counters
  // 现在我们可以简单创造很多计数器
  val count10 = genCounter(10)
  val count99 = genCounter(99)
  //- end
  //- 结束

  // and one more for testing
  // 和一个用来测试
  val testCounter = genCounter(n-1)
  io.tick := testCounter === (n-1).U
  io.cnt := testCounter
}

class NerdCounter(n: Int) extends Counter(n) {

  val N = n

  //- start nerd_counter
  //- 开始傻瓜计数
  val MAX = (N - 2).S(8.W)
  val cntReg = RegInit(MAX)
  io.tick := false.B

  cntReg := cntReg - 1.S
  when(cntReg(7)) {
    cntReg := MAX
    io.tick := true.B
  }
  //- end
  //- 结束

  io.cnt := cntReg.asUInt()
}

object Counter extends App {
  chisel3.Driver.execute(Array("--target-dir", "generated"), () => new WhenCounter(10))
}