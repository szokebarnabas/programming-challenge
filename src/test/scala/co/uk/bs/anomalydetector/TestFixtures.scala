package co.uk.bs.anomalydetector

import co.uk.bs.anomalydetector.model.{ModelConfig, SensorAssignment}

object TestFixtures {

  val firstSensorAssignment = SensorAssignment(
    sensorId = "1354978e-711f-4f26-bd96-6a6735064076",
    model = "UpperBoundThresholdAnomalyDetector",
    threshold = 27.0,
    modelParams = None
  )

  val secondSensorAssignment = SensorAssignment(
    sensorId = "51971af0-8d3d-43b9-b453-bece5ed6eb04",
    model = "UpperBoundThresholdAnomalyDetector",
    threshold = 43.0,
    modelParams = None
  )

  val thirdSensorAssignment = SensorAssignment(
    sensorId = "863beb58-d820-4512-888e-6383bbe1ef77",
    model = "MovingWindowThresholdAnomalyDetector",
    threshold = 33.0,
    modelParams = Some(Map(
      "windowSize" -> "5",
      "foo" -> "bar",
    ))
  )

  val fourthSensorAssignment = SensorAssignment(
    sensorId = "946b5321-43bf-4873-ae7a-39c54ef69692",
    model = "UpperBoundThresholdAnomalyDetector",
    threshold = -33.0,
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
