
// scalaVersion := "2.13.7"
 scalaVersion := "2.12.13"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  // "-Xfatal-warnings",
  // "-Xsource:2.11", // not for 3.5, but for 3.4
  "-language:reflectiveCalls",
  // Enables autoclonetype2
  // "-P:chiselplugin:useBundlePlugin" // not for 3.5, but for 3.4
)

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases")
)

val chiselVersion = "3.5.0"
addCompilerPlugin("edu.berkeley.cs" %% "chisel3-plugin" % chiselVersion cross CrossVersion.full)
libraryDependencies += "edu.berkeley.cs" %% "chisel3" % chiselVersion
libraryDependencies += "edu.berkeley.cs" %% "chisel-iotesters" % "2.5.0"
libraryDependencies += "edu.berkeley.cs" %% "chiseltest" % "0.5.0"

// addCompilerPlugin("edu.berkeley.cs" %% "chisel3-plugin" % "3.5-SNAPSHOT" cross CrossVersion.full)
// libraryDependencies += "edu.berkeley.cs" %% "chisel-iotesters" % "2.5-SNAPSHOT"
// libraryDependencies += "edu.berkeley.cs" %% "chiseltest" % "0.5-SNAPSHOT"

// Chisel 3.4
// this is broken now, as it needs internal stuff for ChiselTest annotations
// addCompilerPlugin("edu.berkeley.cs" % "chisel3-plugin" % "3.4.4" cross CrossVersion.full)
// libraryDependencies += "edu.berkeley.cs" %% "chisel-iotesters" % "1.5.4"
// libraryDependencies += "edu.berkeley.cs" %% "chiseltest" % "0.3.3"
// libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.4" % "test"
