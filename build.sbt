import Dependencies._
import sbt.Resolver

val prometheusExporter = project.in(file("."))
  .settings(
    inThisBuild(List(
      organization := "com.evolutiongaming",
      scalaVersion := "2.12.3"
    )),
    name := "prometheus-exporter",
    licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.html")),
    crossScalaVersions := Seq("2.12.3", "2.11.11"),
    scalacOptions in(Compile, doc) ++= Seq("-no-link-warnings"),
    scalacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-feature",
      "-unchecked",
      "-deprecation",
      "-Xfatal-warnings",
      "-Xlint",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Xfuture"
    ),
    crossScalaVersions := Seq("2.12.3", "2.11.11"),
    resolvers += Resolver.bintrayRepo("evolutiongaming", "maven"),
    homepage := Some(new URL("http://github.com/evolution-gaming/akka-tools")),
    startYear := Some(2017),
    organizationName := "Evolution Gaming",
    organizationHomepage := Some(url("http://evolutiongaming.com")),
    bintrayOrganization := Some("evolutiongaming"),
    releaseCrossBuild := true,
    libraryDependencies ++= Seq(
      prometheusCommonClient, prometheusHotspotClient, simpleClientLogback, Akka.Http, Akka.HttpTestKit) map Dependencies.excludeLog4j,
    parallelExecution in Test := false,
    testOptions in Test ++= Seq(
      Tests.Argument(
        TestFrameworks.ScalaTest,
        "-oDS",
        "-h",
        "target/test-reports"
      )
    ))