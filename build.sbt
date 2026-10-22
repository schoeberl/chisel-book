
// scalaVersion := "2.13.8"

// scalaVersion := "2.13.14"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  // "-Xfatal-warnings",
  "-language:reflectiveCalls",
)

/*
scalaVersion := "2.13.10"
val chiselVersion = "3.5.6"
addCompilerPlugin("edu.berkeley.cs" %% "chisel3-plugin" % chiselVersion cross CrossVersion.full)
libraryDependencies += "edu.berkeley.cs" %% "chisel3" % chiselVersion
libraryDependencies += "edu.berkeley.cs" %% "chiseltest" % "0.5.6"
libraryDependencies += "edu.berkeley.cs" % "ip-contributions" % "0.5.4"
libraryDependencies += "net.fornwall" % "jelf" % "0.9.0"
*/

/*
scalaVersion := "2.13.14"
val chiselVersion = "3.6.1"
addCompilerPlugin("edu.berkeley.cs" %% "chisel3-plugin" % chiselVersion cross CrossVersion.full)
libraryDependencies += "edu.berkeley.cs" %% "chisel3" % chiselVersion
libraryDependencies += "edu.berkeley.cs" %% "chiseltest" % "0.6.2"
libraryDependencies += "net.fornwall" % "jelf" % "0.9.0"
libraryDependencies += "edu.berkeley.cs" % "ip-contributions" % "0.6.1"
*/

/*
scalaVersion := "2.13.14"
val chiselVersion = "5.3.0"
addCompilerPlugin("org.chipsalliance" % "chisel-plugin" % chiselVersion cross CrossVersion.full)
libraryDependencies += "org.chipsalliance" %% "chisel" % chiselVersion
libraryDependencies += "edu.berkeley.cs" %% "chiseltest" % "5.0.2"
libraryDependencies += "net.fornwall" % "jelf" % "0.9.0"
*/


scalaVersion := "2.13.14"
val chiselVersion = "6.5.0"
addCompilerPlugin("org.chipsalliance" % "chisel-plugin" % chiselVersion cross CrossVersion.full)
libraryDependencies += "org.chipsalliance" %% "chisel" % chiselVersion
libraryDependencies += "edu.berkeley.cs" %% "chiseltest" % "6.0.0"
libraryDependencies += "net.fornwall" % "jelf" % "0.9.0"

lazy val gencode = taskKey[Unit]("Extract code snippets from Chisel source files")

gencode := {
  import java.io._
  import scala.io.Source

  def listFiles(folder: String): Unit = {
    val dir = new File(folder)
    if (dir.exists()) {
      dir.listFiles(_.isFile).foreach(f => extract(folder + f.getName))
      dir.listFiles(_.isDirectory).foreach(f => listFiles(folder + f.getName + "/"))
    }
  }

  def extract(f: String): Unit = {
    println(f)
    var code: PrintWriter = null
    val lines = Source.fromFile(f).getLines()
    for (l <- lines) {
      val tokens = l.trim.split(" ")
      if (tokens.length >= 2 && (tokens(0) == "//-" || tokens(0) == "--/")) {
        if (tokens(1) == "start") {
          code = new PrintWriter(new File("code/" + tokens(2) + ".txt"))
        } else if (tokens(1) == "end") {
          if (code != null) { code.close(); code = null }
        }
      } else if (code != null) {
        code.println(l)
      }
    }
    if (code != null) code.close()
  }

  listFiles("src/main/scala/")
  listFiles("src/test/scala/")
  listFiles("src/main/vhdl/")
}

/*
Compile / unmanagedSourceDirectories += baseDirectory.value / "add-src"

scalaVersion := "2.13.18"
libraryDependencies += "org.chipsalliance" %% "chisel" % "7.5.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.18" % Test
libraryDependencies += "net.fornwall" % "jelf" % "0.9.0"
addCompilerPlugin("org.chipsalliance" % "chisel-plugin" % "7.5.0" cross CrossVersion.full)
Test / fork := true
Test / parallelExecution := false

*/