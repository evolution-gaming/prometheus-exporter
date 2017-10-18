# prometheus-exporter

## Overview

prometheus-exporter library which provides exporting metrics from application as Prometheus metrics via akka-http library.
It can be injected as a akka.http.scaladsl.server.Route or can be started as a separate akka-http server.

## Getting Started

``` sbtshell
 resolvers += Resolver.bintrayRepo("evolutiongaming", "maven") 
 libraryDependencies += "com.evolutiongaming" %% "prometheus-exporter" % "0.0.5" 
```

Configuration for http server can be done via https://doc.akka.io/docs/akka-http/current/scala/http/configuration.html

## Metrics, Gauges, Counters etc

```scala
import com.evolutiongaming.metrics.MetricCollectors
import io.prometheus.client._

lazy val collectors: MetricCollectors = new MetricCollectors()

val incomingEventsCounter: Counter =
    collectors.registerCounter(_.name("events_in").help("Kafka input topic messages read"))
    
val fullTimer: Histogram =
    collectors.registerHistogram(_.name("c2k_fullsystem_delay").help("From core to output in ms").labelNames("c2k_time"))    
```

## Embedding as a route
```scala
import com.evolutiongaming.metrics.{MetricResources, Report}

lazy val collectors: MetricCollectors = ...
def metrics = Report(collectors)
val metricsRoute = new MetricResources(metrics).route

val route = metricsRoute ~ otherRoute 
```

## Starting as separateServer

```scala
import com.evolutiongaming.HttpServer

new HttpServer(Seq(metricsRoute, fooRoute, barRoute), "0.0.0.0", "8080")
```