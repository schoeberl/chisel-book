// Extract code snippets from the complete Chisel examples in src

import java.io._
import scala.io.Source

val files = new File("src/main/scala").listFiles
files.map(f => extract("src/main/scala/" + f.getName))

val tfiles = new File("src/test/scala").listFiles
tfiles.map(f => extract("src/test/scala/" + f.getName))

/*
    val writer = new PrintWriter(new File("Write.txt"))

    writer.write("Hello Developer, Welcome to Scala Programming.")
    writer.close()
*/

def extract(f: String) {

  println(f)
  var code: PrintWriter = null
  val lines = Source.fromFile(f).getLines()
  for (l <- lines) {
    val tokens = l.trim.split(" ")
    if (tokens.length >= 2 && tokens(0).equals("//-")) {
      if (tokens(1).equals("start")) {
        code = new PrintWriter(new File("code/"+tokens(2)+".txt"))
//        code.println("\\begin{chisel}")
      } else if (tokens(1).equals("end")) {
//        code.println("\\end{chisel}")
        code.close()
        code = null
      }
    } else if (code != null) {
        code.println(l)
    }
  }

}
