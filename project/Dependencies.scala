import sbt._

object Dependencies {
  private val prometheusVersion = "0.0.26"

  lazy val prometheusCommonClient = "io.prometheus" % "simpleclient_common" % prometheusVersion
  lazy val prometheusHotspotClient = "io.prometheus" % "simpleclient_hotspot" % prometheusVersion
  lazy val simpleClientLogback = "io.prometheus" % "simpleclient_logback" % prometheusVersion

  object Akka {
    private val akkaHttpVersion = "10.0.10"

    val Http = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
    val HttpTestKit = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion
  }


  def excludeLog4j(moduleID: ModuleID) = moduleID.excludeAll(
    ExclusionRule("log4j", "log4j"),
    ExclusionRule("org.slf4j", "slf4j-log4j12"),
    ExclusionRule("commons-logging", "commons-logging"))
}