name := """play-scala"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.11"

libraryDependencies += jdbc
libraryDependencies += evolutions
libraryDependencies += cache
libraryDependencies += ws
libraryDependencies += guice
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.18"
libraryDependencies += "net.liftweb" % "lift-json_2.11" % "3.2.0-M1"
libraryDependencies += "com.typesafe.play" %% "anorm" % "2.5.0"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test


