import chisel3._

//- start param_mod_type、
//- 开始 参数模式类型
class Payload extends Bundle {
  val data = UInt(16.W)
  val flag = Bool()
}
//- end
//- 结束

//- start param_mod
//- 开始 参数模式
class NocRouter[T <: Data](dt: T, n: Int) extends Module {
  val io =IO(new Bundle {
    val inPort = Input(Vec(n, dt))
    val address = Input(Vec(n, UInt(8.W)))
    val outPort = Output(Vec(n, dt))
  })

  // Route the payload according to the address
  // 根据地址路由线路
  // ...
  //- end
  // 结束

  io.outPort(0) := io.inPort(1)
  io.outPort(1) := io.inPort(0)
}

class UseParamRouter() extends Module {
  val io = IO(new Bundle{
    val in = Input(new Payload)
    val inAddr = Input(UInt(8.W))
    val outA = Output(new Payload)
    val outB = Output(new Payload)
  })

  //- start param_mod_use
  //- 开始参数模式使用
  val router = Module(new NocRouter(new Payload, 2))
  //- end
  //- 结束

  // dummy connect to generate Verilog code
  // 冗余连接用于生成Verilog代码
  router.io.inPort(0) := io.in
  router.io.address(0) := io.inAddr
  router.io.inPort(1) := io.in
  router.io.address(1) := io.inAddr+3.U
  io.outA := router.io.outPort(0)
  io.outB := router.io.outPort(1)
}

object UseParamRouter extends App {
  println(chisel3.Driver.emitVerilog(new UseParamRouter()))
}

/*
//- start param_bundle_issue
class Port[T <: Data](dt: T) extends Bundle {
  val address = UInt(8.W)
  val data = dt.cloneType
}
//- end
 */

//- start param_bundle
//- 开始参数捆束
class Port[T <: Data](private val dt: T) extends Bundle {
  val address = UInt(8.W)
  val data = dt.cloneType
}
//- end
//- 结束

//- start param_mod2
//- 开始参数模式2
class NocRouter2[T <: Data](dt: T, n: Int) extends Module {
  val io =IO(new Bundle {
    val inPort = Input(Vec(n, dt))
    val outPort = Output(Vec(n, dt))
  })

  // Route the payload according to the address
  // ...
  //- end

  io.outPort(0) := io.inPort(1)
  io.outPort(1) := io.inPort(0)
}

class UseParamRouter2() extends Module {
  val io = IO(new Bundle{
    val in = Input(new Payload)
    val inAddr = Input(UInt(8.W))
    val outA = Output(new Payload)
    val outB = Output(new Payload)
  })

  //- start param_mod_use2
  val router = Module(new NocRouter2(new Port(new Payload), 2))
  //- end

  // dummy connect to generate Verilog code
  router.io.inPort(0).data := io.in
  router.io.inPort(0).address := io.inAddr
  router.io.inPort(1).data := io.in
  router.io.inPort(1).address := io.inAddr+3.U
  io.outA := router.io.outPort(0).data
  io.outB := router.io.outPort(1).data
}

object UseParamRouter2 extends App {
  println(chisel3.Driver.emitVerilog(new UseParamRouter2()))
}
