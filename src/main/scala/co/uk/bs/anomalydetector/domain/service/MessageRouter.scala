package co.uk.bs.anomalydetector.domain.service

import akka.actor.{Actor, ActorLogging, Props}
import co.uk.bs.anomalydetector.domain.service.Sensor.{SensorState, UpdateState}

class RouterActor extends Actor with ActorLogging {

  override def receive = {
    case msg@UpdateState(newState: SensorState, _) =>
      val actorName = s"sensor_${newState.sensorId}"
      context.child(actorName) match {
        case Some(actorRef) => actorRef ! msg
        case None => context.actorOf(Props[Sensor], actorName) ! msg
      }
  }
}