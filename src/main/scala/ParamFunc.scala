import chisel3._

//- start param_func_type
class ComplexIO extends Bundle {
  val d = UInt(10.W)
  val b = Bool()
}
//- end

class ParamFunc extends Module {
  val io =IO(new Bundle {
    val selA = Input(Bool())
    val resA = Output(UInt(5.W))

    val selB = Input(Bool())
    val resB = Output(new ComplexIO())
  })

  //- start param_func
  def myMux[T <: Data](sel: Bool, tPath: T, fPath: T): T = {

    val ret = WireDefault(fPath)
    when (sel) {
      ret := tPath
    }
    ret
  }
  //- end


  //- start param_func_alt
  def myMuxAlt[T <: Data](sel: Bool, tPath: T, fPath: T): T = {

    val ret = Wire(fPath.cloneType)
    ret := fPath
    when (sel) {
      ret := tPath
    }
    ret
  }
  //- end

  val selA = io.selA
  val selB = io.selB

  //- start param_func_simple
  val resA = myMux(selA, 5.U, 10.U)
  //- end

  /* runtime error with different types
  //- start param_func_wrong
  val resErr = myMux(selA, 5.U, 10.S)
  //- end
   */

  //- start param_func_complex
  val tVal = Wire(new ComplexIO)
  tVal.b := true.B
  tVal.d := 42.U
  val fVal = Wire(new ComplexIO)
  fVal.b := false.B
  fVal.d := 13.U

  // The mulitplexer with a complex type
  val resB = myMux(selB, tVal, fVal)
  //- end


  io.resA := resA
  io.resB := resB

}
