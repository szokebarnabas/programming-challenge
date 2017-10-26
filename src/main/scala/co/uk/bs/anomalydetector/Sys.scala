package co.uk.bs.anomalydetector

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.stream.ActorMaterializer
import akka.util.Timeout

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

trait Sys {
  implicit val actorSystem = ActorSystem("anomaly-detector")
  implicit val executor: ExecutionContext = actorSystem.dispatcher
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = actorSystem.dispatcher
  implicit val timeout = Timeout(10 seconds)

  def createLogger: LoggingAdapter = Logging.getLogger(actorSystem, this)

}
