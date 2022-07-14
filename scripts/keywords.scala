// Spill out Chisel keywords for a Latex table
// call it with: make keywords

// contains some escapes for Latex for _, #, and %
val scala = List(
"\\_", ":", "@", "\\#", "<-", "<:", "<\\%", "=", "=>", ">:", "abstract", "case",
"catch", "class", "def", "do", "else", "extends", "false", "final", "finally",
"for", "forSome", "if", "implicit", "import", "lazy", "match", "new", "null",
"object", "override", "package", "private", "protected", "return", "sealed",
"super", "this", "throw", "trait", "true", "try", "type", "val", "var", "while",
"with", "yield"
)

val chisel = List("when", "otherwise", "elsewhen", "switch", "is",
"io", "clock", "reset", "name",
"Cat", "\\#\\#", ":=",
// Do we include class names in this list?
"Wire", "WireDefault", "Vec", "VecInit",
"Module", "Bool", "UInt", "SInt", "Bits",
"Reg", "RegInit", "RegNext", "RegEnable", "Mem", "SyncMem",
"andR", "orR", "xorR")

def doit(keys: List[String]): Unit = {
  var column = 0
  for (v <- keys.sortBy(_.toLowerCase)) {
    print(s"\\code{$v} ")
    column += 1
    if (column == 6) {
      println("\\\\")
      column = 0
    } else {
      print(" & ")
    }
  }
  if (column != 0) println("\\\\")
  println()
  println()
}

println()
println()
doit(scala)
doit(chisel)



