package co.uk.bs.anomalydetector.domain.service

import akka.actor.{Actor, ActorLogging, Props}
import co.uk.bs.anomalydetector.domain.service.Sensor.UpdateState
import co.uk.bs.anomalydetector.model.Event
import co.uk.bs.dto.EventDto

class RouterActor extends Actor with ActorLogging {

  override def receive = {

    case event@EventDto(_, sensorId, _, _) =>
      val actorName = s"sensor_${sensorId}"
      val msg = UpdateState(state = Event(event.eventId, event.sensorId, event.timestamp, event.value))
      context.child(actorName) match {
        case Some(actorRef) => actorRef forward msg
        case None => context.actorOf(Props[Sensor], actorName) forward msg
      }
  }
}