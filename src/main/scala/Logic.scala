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
  // hexadecimal representation of 255
  // 255的十二进制
  "hff".U  
  // octal representation of 255
  // 255的八进制    
  "o377".U 
  // binary representation of 255
  // 255的二进制      
  "b1111_1111".U 
  // 结束
  //- end

  //- start bool
  //- 开始逻辑
  Bool()
  true.B
  false.B
  //- 结束
  //- end

  val a = io.a
  val b = io.b
  val c = io.c
  //- start logic
  //- 开始逻辑
  val logic = (a & b) | c
  //- 结束
  //- end

  io.out := logic

  val bt = true.B
  val bf = false.B

  val bop = bt & bf

  //- start bool_ops
  //- 开始逻辑操作
  // bitwise and
  // 按位与
  val and = a & b 
  // bitwise or
  // 按位或
  val or = a | b  
  // bitwise xor
  // 按位异或
  val xor = a ^ b 
  // bitwise negation
  // 按位非
  val not = ~a    
  //- end
  //- 结束

  //- start arith_ops
  //- 开始算术操作
  // addition
  // 相加
  val add = a + b 
  // subtraction
  // 相减
  val sub = a - b 
  // negate
  // 相反数
  val neg = -a  
  // multiplication
  // 相乘  
  val mul = a * b 
  // division
  // 相除
  val div = a / b 
  // modulo operation
  // 取余数
  val mod = a % b 
  //- end
  //- 结束
  

  //- start wire
  //- 开始线
  val w = Wire(UInt())

  w := a & b
  //- end
  //- 结束

  val x = 123.U
  //- start single_bit
  //- 开始单位元
  val sign = x(31)
  //- end
  //- 结束

  val largeWord = 1.U
  //- start sub_field
  //- 开始部分域
  val lowByte = largeWord(7, 0)
  //- end
  //- 结束

  val highByte = 0xff.U

  //- start concat
  //- 开始串联
  val word = Cat(highByte, lowByte)
  //- end
  //- 结束

  val sel = b === c
  //- start mux
  //- 开始复用器
  val result = Mux(sel, a, b)
  //- end
  //- 结束
}
