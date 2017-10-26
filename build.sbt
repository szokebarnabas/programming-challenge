import sbt.Keys._

lazy val root = (project in file(".")).
  settings(
    name := "anomalydetector",
    version := "1.0",
    organization := "co.uk.bs",
    scalaVersion := "2.12.1",
    mainClass in Compile := Some("co.uk.bs.anomalydetector.Application")
  )

resolvers += "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-http_2.12" % "10.0.10",
  "com.typesafe.akka" % "akka-slf4j_2.12" % "2.4.17",
  "ch.qos.logback" % "logback-classic" % "1.2.1",
  "de.heikoseeberger" % "akka-http-json4s_2.12" % "1.13.0",
  "org.json4s" % "json4s-jackson_2.12" % "3.5.0",
  "org.json4s" % "json4s-ext_2.12" % "3.5.0",
  "com.typesafe" % "config" % "1.3.1",
  "com.typesafe.akka" % "akka-http-testkit_2.12" % "10.0.5" % "test",
  "org.scalatest" % "scalatest_2.12" % "3.0.4" % "test"
)

assemblyJarName in assembly := s"${name.value}.jar"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case "application.conf" => MergeStrategy.concat
  case "reference.conf" => MergeStrategy.concat
  case x => MergeStrategy.first
}