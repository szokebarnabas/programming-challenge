package co.uk.bs.anomalydetector.domain.service

import akka.actor.{Actor, ActorLogging}
import co.uk.bs.anomalydetector.domain.service.Sensor.UpdateState
import co.uk.bs.anomalydetector.model._
import co.uk.bs.dto.DetectionResultDto

import scala.util.{Failure, Success, Try}

class Sensor extends Actor with ActorLogging {

  override def receive = updated(EventStore(Seq.empty))

  private def updated(eventStore: EventStore): Receive = {

    case UpdateState(newEvent: Event) =>
      val updatedStore = eventStore.update(newEvent)

      val result = ModelConfigStore.modelConfig.modelMapping.find(sa => sa.sensorId == newEvent.sensorId) match {
        case Some(config) =>
          Try {
            config.model match {
              case "UpperBoundThresholdAnomalyDetector" => new UpperBoundStrategy().evaluate(updatedStore, config.threshold)
              case invalid => throw new IllegalStateException(s"Invalid model: $invalid")
            }

          } match {
            case Success(detectionResult) => createResult(event = newEvent, detectionResult)
            case Failure(t) =>
              log.error(t, "Failed to evaluate sensor data.")
              createResult(
                event = newEvent,
                status = ERROR,
                cause = t.getMessage,
                message = "Failed to evaluate sensor data.")
          }
        case None =>
          createResult(
            event = newEvent,
            status = NO_MODEL,
            cause = "Model not found of sensor",
            message = "Model not found of sensor"
          )
      }

      log.info(s"Detection result of sensor ${newEvent.sensorId}: $result")

      sender() ! result
      context.become(updated(updatedStore))
  }

  def createResult(event: Event,
                   status: DetectionResult,
                   cause: String = "",
                   message: String = ""): DetectionResultDto = {

    DetectionResultDto(
      eventId = event.eventId,
      sensorId = event.sensorId,
      timestamp = System.currentTimeMillis(),
      value = event.value,
      status = status.toString,
      cause = cause,
      message = message
    )
  }

}

object Sensor {

  case class UpdateState(state: Event)

}
