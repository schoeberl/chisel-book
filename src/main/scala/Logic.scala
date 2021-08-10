import chisel3._
import chisel3.util._

class Logic extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(1.W))
    val b = Input(UInt(1.W))
    val c = Input(UInt(1.W))
    val out = Output(UInt(1.W))
    val cat = Output(UInt(16.W))
    val ch = Output(UInt(8.W))
    val word = Output(UInt(16.W))
    val result = Output(UInt(4.W))
  })

  //- start types
  Bits(8.W)
  UInt(8.W)
  SInt(10.W)
  //- end

  val n = 10

  //- start n_w
  n.W
  Bits(n.W)
  //- end

  //- start constants
  0.U  // defines a UInt constant of 0
  -3.S // defines a SInt constant of -3
  //- end

  //- start const_width
  3.U(4.W) // An 4-bit constant of 3
  //- end

  //- start const_base
  "hff".U        // hexadecimal representation of 255
  "o377".U       // octal representation of 255
  "b1111_1111".U // binary representation of 255
  //- end

  //- start const_char
  val aChar = 'A'.U
  //- end
  io.ch := aChar

  //- start bool
  Bool()
  true.B
  false.B
  //- end

  val a = io.a
  val b = io.b
  val c = io.c
  //- start logic
  val logic = (a & b) | c
  //- end

  io.out := logic

  val bt = true.B
  val bf = false.B

  val bop = bt & bf

  //- start bool_ops
  val and = a & b // bitwise and
  val or = a | b  // bitwise or
  val xor = a ^ b // bitwise xor
  val not = ~a    // bitwise negation
  //- end

  //- start arith_ops
  val add = a + b // addition
  val sub = a - b // subtraction
  val neg = -a    // negate
  val mul = a * b // multiplication
  val div = a / b // division
  val mod = a % b // modulo operation
  //- end

  //- start wire
  val w = Wire(UInt())

  w := a & b
  //- end

  val x = 123.U
  //- start single_bit
  val sign = x(31)
  //- end

  val largeWord = 1.U
  //- start sub_field
  val lowByte = largeWord(7, 0)
  //- end

  val highByte = 0xff.U

  //- start concat
  val word = highByte ## lowByte
  //- end
  io.cat := word

  val sel = b === c
  //- start mux
  val result = Mux(sel, a, b)
  //- end

  /*
  //- start no_partial_assign
  val assignWord = Wire(UInt(16.W))

  assignWord(7, 0) := lowByte
  assignWord(15, 8) := highByte
  //- end
  */

  //- start partial_assign_solution
  val assignWord = Wire(UInt(16.W))

  class Split extends Bundle {
    val high = UInt(8.W)
    val low = UInt(8.W)
  }

  val split = Wire(new Split())
  split.low := lowByte
  split.high := highByte

  assignWord := split.asUInt()
  //- end
  io.word := assignWord

  val data = 5.U(4.W)
  //- start partial_vec_bool
  val vecResult = Wire(Vec(4, Bool()))

  // example assignments
  vecResult(0) := data(0)
  vecResult(1) := data(1)
  vecResult(2) := data(2)
  vecResult(3) := data(3)

  val uintResult = vecResult.asUInt
  //- end
  io.result := uintResult

}
