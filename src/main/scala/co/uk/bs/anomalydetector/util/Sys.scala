package co.uk.bs.anomalydetector.util

import akka.actor.{ActorSystem, Props}
import akka.event.{Logging, LoggingAdapter}
import akka.stream.ActorMaterializer
import akka.util.Timeout
import co.uk.bs.anomalydetector.domain.service.RouterActor

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

trait Sys {
  implicit val system = ActorSystem("anomaly-detector")
  implicit val executor: ExecutionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  implicit val timeout = Timeout(10 seconds)
  val routerActor = system.actorOf(Props[RouterActor], "routerActor")

  def createLogger: LoggingAdapter = Logging.getLogger(system, this)

}
