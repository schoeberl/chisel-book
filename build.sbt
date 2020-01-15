
scalaVersion := "2.11.7"

// libraryDependencies += "edu.berkeley.cs" %% "chisel" % "latest.release"

// libraryDependencies += "edu.berkeley.cs" %% "chisel" % "2.2.38"

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases")
)

scalacOptions := Seq("-deprecation")

libraryDependencies += "edu.berkeley.cs" %% "chisel3" % "3.2.2"
libraryDependencies += "edu.berkeley.cs" %% "chisel-iotesters" % "1.3.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
