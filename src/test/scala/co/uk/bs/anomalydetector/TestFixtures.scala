package co.uk.bs.anomalydetector

import co.uk.bs.anomalydetector.domain.service.Sensor.UpdateState
import co.uk.bs.anomalydetector.model.{Event, UpperBound}
import co.uk.bs.dto.DetectionResultDto

object TestFixtures {

  val event = Event(eventId = "ev1", sensorId = "sensor1", timestamp = 123456, value = 10.5, modelType = UpperBound)
  val sensorRequest = UpdateState(state = event)
  val sensorResponse = DetectionResultDto(
    eventId = "ev1",
    sensorId = "sensor1",
    timestamp = 123456,
    value = 10.5,
    status = "NO_MODEL",
    cause = "",
    message = ""
  )
}
