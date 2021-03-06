import Dependencies._
import sbt.Resolver

val prometheusExporter = project.in(file("."))
  .settings(
    inThisBuild(List(
      organization := "com.evolutiongaming",
      scalaVersion := "2.12.4",
      developers := List(Developer("stas", "Stas", "stass.ua@gmail.com", url("https://www.linkedin.com/in/stas-shevchenko-916b7a16"))),
      scmInfo := Some(ScmInfo(url("https://github.com/evolution-gaming/prometheus-exporter"), "scm:git:git@github.com:evolution-gaming/prometheus-exporter.git")),
      releaseEarlyWith := BintrayPublisher,
    )),
    name := "prometheus-exporter",
    licenses := Seq("MIT" -> url("http://www.opensource.org/licenses/mit-license.html")),
    crossScalaVersions := Seq("2.12.4", "2.11.11"),
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
    resolvers += Resolver.bintrayRepo("evolutiongaming", "maven"),
    releaseEarlyEnableLocalReleases := true,
    homepage := Some(new URL("https://github.com/evolution-gaming/prometheus-exporter")),
    startYear := Some(2017),
    organizationName := "Evolution Gaming",
    organizationHomepage := Some(url("http://evolutiongaming.com")),
    bintrayOrganization := Some("evolutiongaming"),
    libraryDependencies ++= Seq(
      prometheusCommonClient, prometheusHotspotClient, simpleClientLogback, logging,
      Akka.Http, Akka.HttpTestKit % Test, scalatest % Test, pegdown % Test) map Dependencies.excludeLog4j,
    testOptions in Test ++= Seq(
      Tests.Argument(
        TestFrameworks.ScalaTest,
        "-oDS",
        "-h",
        "target/test-reports"
      )
    ))