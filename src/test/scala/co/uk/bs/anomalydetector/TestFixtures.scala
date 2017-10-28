package co.uk.bs.anomalydetector

import co.uk.bs.anomalydetector.domain.service.Sensor.UpdateState
import co.uk.bs.anomalydetector.model.{Event, ModelConfig, SensorAssignment, UpperBound}
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

  val firstSensorAssignment = SensorAssignment(
    sensorId = "1354978e-711f-4f26-bd96-6a6735064076",
    model = "ModelA",
    threshold = 27.0,
    modelParams = None
  )

  val secondSensorAssignment = SensorAssignment(
    sensorId = "51971af0-8d3d-43b9-b453-bece5ed6eb04",
    model = "ModelB",
    threshold = 43.0,
    modelParams = None
  )

  val thirdSensorAssignment = SensorAssignment(
    sensorId = "863beb58-d820-4512-888e-6383bbe1ef77",
    model = "ModelB",
    threshold = 33.0,
    modelParams = None
  )

  val fourthSensorAssignment = SensorAssignment(
    sensorId = "863beb58-d820-4512-888e-6383bbe1ef77",
    model = "ModelC",
    threshold = 33.0,
    modelParams = Some(Map(
      "windowSize" -> "5",
      "foo" -> "bar",
    ))
  )

  val expectedConfig = ModelConfig(
    Seq(
      firstSensorAssignment,
      secondSensorAssignment,
      thirdSensorAssignment,
      fourthSensorAssignment,
    ))
}
