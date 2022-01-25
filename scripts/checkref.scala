// Check URLs
// call it with: make checkref

import java.io._
import scala.io.Source
import scala.util.matching.Regex

val pattern: Regex = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)".r

val lines = Source.fromFile("chisel-book.tex").mkString

for (pM <- pattern.findAllMatchIn(lines)) {
  try {
    val html = Source.fromURL(pM.toString())
    // this finds false positives (errors), but they are just a few to check manually
    html.getLines().next()
    // println(pM + " is good")
  } catch {
    case e: Exception => println(pM + " is not good")
  }
}

