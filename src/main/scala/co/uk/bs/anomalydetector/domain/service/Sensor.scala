package co.uk.bs.anomalydetector.domain.service

import akka.actor.{Actor, ActorLogging}
import co.uk.bs.anomalydetector.domain.service.Sensor.{SensorState, StateStore, UpdateState}
import co.uk.bs.anomalydetector.model.DetectionModelType

object Sensor {

  case class StateStore(events: Seq[SensorState]) {
    def update(state: SensorState) = StateStore(events ++ (Seq(state)))
  }

  case class UpdateState(state: SensorState, model: DetectionModelType)

  case class SensorState(sensorId: String, value: Double)

}

class Sensor extends Actor with ActorLogging {

  override def receive = updated(StateStore(Seq.empty))

  private def updated(stateStore: StateStore): Receive = {
    case UpdateState(newState: SensorState, model: DetectionModelType) => ???
  }
}