package com.evolutiongaming.metrics

import java.{util => ju}

import io.prometheus.client._
import io.prometheus.client.hotspot._
import io.prometheus.client.logback.InstrumentedAppender.{COUNTER_NAME => LogCounterName}

import scala.collection.JavaConverters._


/** Wrapper around Prometheus' collector registry
  * Ensures that
  * - both standard & service specific collectors registered
  * - logging collectors samples available for exposure
  *
  * Since [[io.prometheus.client.logback.InstrumentedAppender]]
  * registers collectors at default registry log samples are combined with
  * all other samples registered at separate registry.
  *
  * With a slight overhead of having two registries this design allows to
  * avoid static dependencies all over the place
  */
class MetricCollectors {
  val registry: CollectorRegistry = new CollectorRegistry(true)

  registerStandardCollectors()

  private def registerStandardCollectors(): Unit = {
    val standardCollectors = Seq(
      new StandardExports,
      new MemoryPoolsExports,
      new GarbageCollectorExports,
      new ThreadExports,
      new ClassLoadingExports,
      new VersionInfoExports
    )
    standardCollectors foreach register
  }

  def register[C <: Collector](c: C): C =
    c register registry

  def registerCounter(configure: Counter.Builder => Counter.Builder): Counter =
    register(configure(Counter.build()).create)

  def metricFamilySamples: ju.Enumeration[Collector.MetricFamilySamples] = {
    val combined =
      registry.metricFamilySamples.asScala ++
        CollectorRegistry.defaultRegistry.metricFamilySamples.asScala.collect {
          case samples if samples.name == LogCounterName => samples
        }
    combined.asJavaEnumeration
  }
}