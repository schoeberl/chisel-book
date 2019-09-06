import chisel3._

//- start param_adder
//- 开始参数加法器
class ParamAdder(n: Int) extends Module {
  val io = IO(new Bundle{
    val a = Input(UInt(n.W))
    val b = Input(UInt(n.W))
    val c = Output(UInt(n.W))
  })

  io.c := io.a + io.b
}
//- end
//- 结束

class UseAdder extends Module {
  val io = IO(new Bundle{
    val x = Input(UInt(16.W))
    val y = Input(UInt(16.W))
    val res = Output(UInt(16.W))
  })

  //- start use_param_adder
  //- 开始使用参数加法器
  val add8 = Module(new ParamAdder(8))
  val add16 = Module(new ParamAdder(16))
  //- end
  //- 结束

  add16.io.a := io.x
  add16.io.b := io.y
  io.res := add16.io.c | add8.io.c

  add8.io.a := io.x
  add8.io.b := io.y
}

object UseAdder extends App {
  // chisel3.Driver.execute(Array[String](), () => new UseAdder())
  println(chisel3.Driver.emitVerilog(new UseAdder()))
}
