name := "ready-kafka"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.kafka" % "kafka-clients" % "0.10.0.1"
libraryDependencies += "com.typesafe.play" % "play-json_2.11" % "2.5.6"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"