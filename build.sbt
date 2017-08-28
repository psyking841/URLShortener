import sbt.Keys._

name := "URLShortener"

version := "0.1"

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  //"org.scala-lang" % "scala-library" % "2.11.11",
  "org.postgresql" % "postgresql" % "42.1.4",
  "org.slf4j" % "slf4j-simple" % "1.7.13",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)