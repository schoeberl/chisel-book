import chisel3._
import chisel3.util._

class Logic extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(1.W))
    val b = Input(UInt(1.W))
    val c = Input(UInt(1.W))
    val out = Output(UInt(1.W))
  })

  //- start types
  //- 开始类型
  Bits(8.W)
  UInt(8.W)
  SInt(10.W)
  //- end
  //-结束

  val n = 10

  //- start n_w
  //- 开始n的位宽
  n.W
  Bits(n.W)
  //- end
  //- 结束

  //- start constants
  //- 开始常量
  // defines a UInt constant of 0
  // 定义一个UInt常量0
  0.U  
  // defines a SInt constant of -3
  // 定义一个SInt常量-3
  -3.S 
  //- end
  //- 结束

  //- start const_width
  //- 开始 常量宽度
  8.U(4.W) 
  // An 4-bit constant of 8
  // 一个4位常量8

  //- end
  //- 结束

  //- start const_base
  //- 开始
  "hff".U        // hexadecimal representation of 255
  "o377".U       // octal representation of 255
  "b1111_1111".U // binary representation of 255
  //- end

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
  val word = Cat(highByte, lowByte)
  //- end

  val sel = b === c
  //- start mux
  val result = Mux(sel, a, b)
  //- end
}
