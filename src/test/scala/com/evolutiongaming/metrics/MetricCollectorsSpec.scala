package com.evolutiongaming.metrics

import org.scalatest.FunSuite

class MetricCollectorsSpec extends FunSuite {
  test("Expose JVM metrics") {
    val collectors = new MetricCollectors()

    assert(Report(collectors).text contains "# HELP jvm_info JVM version info")
  }
}
