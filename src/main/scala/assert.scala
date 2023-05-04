class SimpleAssert {

  def add(a: Int,b: Int)= {
    val sum = a + b

    assert(sum >= a)
    assert(sum >= b)
    sum
  }

}
