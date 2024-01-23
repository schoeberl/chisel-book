import chisel3._

class Convert extends Module {

  val io = IO(new Bundle {
    val vec = Input(Vec(4, UInt(8.W)))
    val int32 = Output(UInt(32.W))

    val in = Input(UInt(8.W))
    val in2 = Input(UInt(16.W))
    val out = Output(UInt(24.W))
  })

  //- start convert_vec2uint
  val vec = Wire(Vec(4, UInt(8.W)))
  val word = vec.asUInt
  //- end

  vec := io.vec
  io.int32 := word

  //- start convert_uint2vec
  val vec2 = word.asTypeOf(Vec(4, UInt(8.W)))
  //- end


  assert(vec2(0) === vec(0))
  assert(vec2(1) === vec(1))
  assert(vec2(2) === vec(2))
  assert(vec2(3) === vec(3))

  //- start convert_bundle2uint
  class MyBundle extends Bundle {
    val a = UInt(8.W)
    val b = UInt(16.W)
  }

  val bundle = Wire(new MyBundle)
  val word2 = bundle.asUInt
  //- end

  //- start convert_uint2bundle
  val bundle2 = word2.asTypeOf(new MyBundle)
  //- end

  //- start convert_zero2bundle
  val bundle3 = 0.U.asTypeOf(new MyBundle)
  //- end

  assert(bundle3.a === 0.U)
  assert(bundle3.b === 0.U)

  assert(bundle2.a === bundle.a)
  assert(bundle2.b === bundle.b)

  bundle.a := io.in
  bundle.b := io.in2
  io.out := word2

}
