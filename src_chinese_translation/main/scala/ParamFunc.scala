import chisel3._

//- start param_func_type
//- 开始参数函数类型
class ComplexIO extends Bundle {
  val d = UInt(10.W)
  val b = Bool()
}
//- end
//- 结束

class ParamFunc extends Module {
  val io =IO(new Bundle {
    val selA = Input(Bool())
    val resA = Output(UInt(5.W))

    val selB = Input(Bool())
    val resB = Output(new ComplexIO())
  })

  //- start param_func
  //- 开始参数函数
  def myMux[T <: Data](sel: Bool, tPath: T, fPath: T): T = {

    val ret = WireInit(fPath)
    when (sel) {
      ret := tPath
    }
    ret
  }
  //- end
  //- 结束


  //- start param_func_alt
  //- 开始参数函数切换
  def myMuxAlt[T <: Data](sel: Bool, tPath: T, fPath: T): T = {

    val ret = Wire(fPath.cloneType)
    ret := fPath
    when (sel) {
      ret := tPath
    }
    ret
  }
  //- end
  //- 结束

  val selA = io.selA
  val selB = io.selB

  //- start param_func_simple
  //- 开始简单参数函数
  val resA = myMux(selA, 5.U, 10.U)
  //- end
  //- 结束

  /* runtime error with different types
  //- start param_func_wrong
  val resErr = myMux(selA, 5.U, 10.S)
  //- end
   */

  //- start param_func_complex
  //- 开始复杂参数函数
  val tVal = Wire(new ComplexIO)
  tVal.b := true.B
  tVal.d := 42.U
  val fVal = Wire(new ComplexIO)
  fVal.b := false.B
  fVal.d := 13.U

  // The mulitplexer with a complex type
  // 复杂类型的复用器
  val resB = myMux(selB, tVal, fVal)
  //- end
  //- 结束


  io.resA := resA
  io.resB := resB

}
