package com.evolutiongaming.metrics

import org.scalatest.FunSuite

class ReportSpec extends FunSuite {
  test("Generates report for counter") {

    val collectors = new MetricCollectors()

    val counter = collectors.registerCounter(_
      .name("http_requests_total")
      .help("Total number of received HTTP requests")
    )

    counter.inc(5.01)

    val report = Report(collectors)

    assert(report.text contains "# HELP http_requests_total Total number of received HTTP requests")
    assert(report.text contains "# TYPE http_requests_total counter")
    assert(report.text contains "http_requests_total 5.01")
  }
}