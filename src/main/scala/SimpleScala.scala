class SimpleScala {


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

  //- start scala_loop_comment
  // Loops i from 0 to 9
  for (i <- 0 until 10) {
    // use i to index into a Wire or Vec
  }
  //- end



  //- start scala_condition
  for (i <- 0 until 10) {
    print(i)
    if (i%2 == 0) {
      println(" is even")
    } else {
      println(" is odd")
    }
  }
  //- end

  //- start scala_tuple
  val city = (2000, "Frederiksberg")
  val zipCode = city._1
  val name = city._2
  //- end

  //- start scala_seq
  val numbers = Seq(1, 15, -2, 0)
  val second = numbers(1)
  //- end
}
