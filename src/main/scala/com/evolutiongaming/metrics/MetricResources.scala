package com.evolutiongaming.metrics


import akka.http.scaladsl.coding.Gzip
import akka.http.scaladsl.marshalling.Marshaller._
import akka.http.scaladsl.marshalling._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.util.ByteString

class MetricResources(metricsReport: => Report) {

  import MetricResources._

  def route: Route = get {
    path("metrics") {
      encodeResponseWith(Gzip) {
        complete(metricsReport)
      }
    }
  }
}

object MetricResources {

  class InvalidContentType(description: String)
    extends Exception(
      s"Invalid content type for metrics resource: '${Report.ContentType}'. $description")

  private val TextReportContentType =
    ContentType.parse(Report.ContentType).fold(
      err => throw new InvalidContentType(err.map(_.formatPretty).mkString),
      identity
    )

  implicit val ReportMarshaller: ToEntityMarshaller[Report] =
    byteStringMarshaller(TextReportContentType).compose[Report](r => ByteString(r.text))

}