//- start case_class
case class Config(txDepth: Int, rxDepth: Int, width: Int)
//- end

object Config extends App {

  //- start case_class_use
  val param = Config(4, 2, 16)

  println("The width is " + param.width)
  //- end

  // val error = SaveConfig(-1, 2, 3)
}

//- start case_class_save
case class SaveConf(txDepth: Int, rxDepth: Int, width: Int) {

  assert(txDepth > 0 && rxDepth > 0 && width > 0, "parameters must be larger than 0")
}
//- end