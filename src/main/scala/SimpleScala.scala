import chisel3._

class SimpleScala extends Module {

  val io = new Bundle() {}

  /*
  //- start scala_val
  // A value is a constant
  val zero = 0
  // No new assignment is possible
  // The following will not compile
  zero = 3
  //- end
  */

  //- start scala_var
  // We can change the value of a var variable
  var x = 2
  x = 3
  //- end

  //- start scala_int_type
  val number: Int = 42
  //- end

  var v = "Hello"
  v = "Hello World"

  // Type usually inferred, but can be declared
  var s: String = "abc"

  //- start scala_loop
  // Loops from 0 to 9
  // Automatically creates loop value i
  for (i <- 0 until 10) {
    println(i)
  }
  //- end

  val inVal = 1.U

  //- start scala_loop_gen
  val shiftReg = RegInit(0.U(8.W))

  shiftReg(0) := inVal

  for (i <- 1 until 8) {
    shiftReg(i) := shiftReg(i-1)
  }
  //- end

  //- start scala_condition
  for (i <- 0 until 10) {
    if (i%2 == 0) {
      println(i + " is even")
    } else {
      println(i + " is odd")
    }
  }
  //- end
}
