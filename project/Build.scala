import sbt._
import sbt.Keys._

object AtpWorldTourScalaBuild extends Build {

  val AtpWorldTourVersion = "4.0.0"
  val AtpWorldTourShellVersion = AtpWorldTourVersion
  val AtpWorldTourCypherVersion = AtpWorldTourVersion
  val AtpWorldTourRestVersion = AtpWorldTourVersion


  lazy val AtpWorldTourScala = Project(
    id = "atpworldtour-api",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "AtpWorldTour",
      fork := true,
      organization := "org.atpworldtour",
      version := "0.2",
      scalaVersion := "2.9.2",
      resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
      libraryDependencies ++= Seq("junit" % "junit" % "4.4",
                                  "org.jsoup" % "jsoup" % "1.6.1",
                                  "commons-io" % "commons-io" % "2.1",
                                  "joda-time" % "joda-time" % "2.0",
                                  "org.joda" % "joda-convert" % "1.1",
                                  "org.scala-lang" % "scala-compiler" % "2.9.2")

    )
  )
}


