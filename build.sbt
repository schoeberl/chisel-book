
scalaVersion := "2.11.7"

// libraryDependencies += "edu.berkeley.cs" %% "chisel" % "latest.release"

// libraryDependencies += "edu.berkeley.cs" %% "chisel" % "2.2.38"

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases")
)

scalacOptions := Seq("-deprecation")

libraryDependencies += "edu.berkeley.cs" %% "chisel3" % "3.0.1"
libraryDependencies += "edu.berkeley.cs" %% "chisel-iotesters" % "1.1.0"