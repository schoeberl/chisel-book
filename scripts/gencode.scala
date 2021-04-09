// Extract code snippets from the complete Chisel examples in src

import java.io._
import scala.io.Source

val files = new File("src/main/scala").listFiles
  .map(f => extract("src/main/scala/" + f.getName))

val iotfiles = new File("src/test/scala/iotesters").listFiles
  .map(f => extract("src/test/scala/iotesters/" + f.getName))
val cttfiles = new File("src/test/scala/testers2").listFiles
  .map(f => extract("src/test/scala/testers2/" + f.getName))

val tfiles = iotfiles ++ cttfiles

def extract(f: String) {
  println(f)
  var code: PrintWriter = null
  val lines = Source.fromFile(f).getLines()
  for (l <- lines) {
    val tokens = l.trim.split(" ")
    if (tokens.length >= 2 && tokens(0).equals("//-")) {
      if (tokens(1).equals("start")) {
        code = new PrintWriter(new File("code/"+tokens(2)+".txt"))
      } else if (tokens(1).equals("end")) {
        code.close()
        code = null
      }
    } else if (code != null) {
        code.println(l)
    }
  }
}
