package com.evolutiongaming

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.RouteConcatenation._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Materializer, Supervision}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Future
import scala.util.{Failure, Success}

class HttpServer(routees: Seq[Route], interface: String = "0.0.0.0", port: Int = 8087)
  (implicit system: ActorSystem) extends LazyLogging {

  import system.dispatcher

  implicit val materializer: Materializer = {
    val decider: Supervision.Decider = { e =>
      logger.error("Unhandled exception in stream", e)

      Supervision.Stop
    }

    val settings = ActorMaterializerSettings(system).withSupervisionStrategy(decider)
    ActorMaterializer(settings)
  }

  private val value: Route = routees.reduce(_ ~ _)
  private val server: Future[ServerBinding] = bind(value)

  server onComplete {
    case Success(_) => logger.info(s"HTTP server started on $interface:$port")
    case Failure(e) =>
      logger.error(s"HTTP server failed to start on $interface:$port, terminating", e)
      system.terminate()                                t
  }

  protected def bind(route: Route)(implicit exceptionHandler: ExceptionHandler = ExceptionHandler.default(implicitly)): Future[ServerBinding] =
    Http().bindAndHandle(route, interface, port)

  system.registerOnTermination(server.flatMap(_.unbind())(system.dispatcher))
}