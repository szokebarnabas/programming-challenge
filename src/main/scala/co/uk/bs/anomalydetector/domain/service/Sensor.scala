package co.uk.bs.anomalydetector.domain.service

import akka.actor.{Actor, ActorLogging}
import co.uk.bs.anomalydetector.domain.service.Sensor.{EventStore, UpdateState}
import co.uk.bs.anomalydetector.model.Event
import co.uk.bs.dto.DetectionResultDto

object Sensor {

  case class EventStore(events: Seq[Event]) {
    def update(state: Event) = EventStore(events ++ (Seq(state)))
  }
  case class UpdateState(state: Event)
}

class Sensor extends Actor with ActorLogging {

  override def receive = updated(EventStore(Seq.empty))

  private def updated(eventStore: EventStore): Receive = {
    case UpdateState(newEvent: Event) =>

      val result = DetectionResultDto(
        eventId = newEvent.eventId,
        sensorId = newEvent.sensorId,
        timestamp = System.currentTimeMillis(),
        value = newEvent.value,
        status = "NO_MODEL",
        cause = "",
        message = ""
      )

      log.info(s"Detection result of sensor ${newEvent.sensorId}: $result")

      sender() ! result
  }

}