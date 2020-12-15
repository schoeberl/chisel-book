
scalaVersion := "2.12.12"

scalacOptions := Seq("-deprecation", "-Xsource:2.11")

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases")
)


// libraryDependencies += "edu.berkeley.cs" %% "chisel3" % "3.2.2"
// libraryDependencies += "edu.berkeley.cs" %% "chisel-iotesters" % "1.3.2"

// TODO: towards Chisel x.3/4
libraryDependencies += "edu.berkeley.cs" %% "chisel-iotesters" % "1.4.2"
libraryDependencies += "edu.berkeley.cs" %% "chiseltest" % "0.2.2"

