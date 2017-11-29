
scalaVersion := "2.11.7"

// Only needed if not organized according to the sbt standard
// scalaSource in Compile := baseDirectory.value / "src"

// This is the latest release from UCB
// libraryDependencies += "edu.berkeley.cs" %% "chisel" % "latest.release"

// use 2.2.30 till VCD issue is fixed
 libraryDependencies += "edu.berkeley.cs" %% "chisel" % "2.2.33"

// This is from a locally published version
// libraryDependencies += "edu.berkeley.cs" %% "chisel" % "2.3-SNAPSHOT"
