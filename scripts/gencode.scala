// Extract code snippets from the complete Chisel examples in src

import java.io._
import scala.io.Source

  list("src/main/scala/")
  list("src/test/scala/")
  list("src/main/vhdl/")

def list(folder: String): Unit = {
  new File(folder).listFiles(_.isFile).map(f => extract(folder + f.getName))
  new File(folder).listFiles(_.isDirectory).map(f => list(folder + f.getName + "/"))
}

def extract(f: String) {
  println(f)
  var code: PrintWriter = null
  val lines = Source.fromFile(f).getLines()
  for (l <- lines) {
    val tokens = l.trim.split(" ")
    if (tokens.length >= 2 && (tokens(0).equals("//-") || tokens(0).equals("--/"))) {
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
