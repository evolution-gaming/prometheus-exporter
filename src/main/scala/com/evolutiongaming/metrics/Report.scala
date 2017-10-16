package com.evolutiongaming.metrics

import java.io.CharArrayWriter

import io.prometheus.client.exporter.common.TextFormat


case class Report(text: String)

object Report {
  private val InitSize: Int = 16 * 1024
  val ContentType: String = TextFormat.CONTENT_TYPE_004

  def apply(cs: MetricCollectors): Report = {
    val writer = new CharArrayWriter(InitSize)
    TextFormat.write004(writer, cs.metricFamilySamples)
    Report(writer.toString)
  }
}