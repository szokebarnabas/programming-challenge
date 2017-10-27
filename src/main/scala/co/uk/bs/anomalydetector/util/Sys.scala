package co.uk.bs.anomalydetector.util

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.stream.ActorMaterializer
import akka.util.Timeout

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

trait Sys {
  implicit val system = ActorSystem("anomaly-detector")
  implicit val executor: ExecutionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  implicit val timeout = Timeout(10 seconds)

  def createLogger: LoggingAdapter = Logging.getLogger(system, this)

}
