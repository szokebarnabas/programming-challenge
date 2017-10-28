package co.uk.bs.anomalydetector

package object model {

  sealed trait DetectionResult
  final case object NO_ANOMALY extends DetectionResult
  final case object ANOMALY extends DetectionResult
  final case object ERROR extends DetectionResult
  final case object NO_MODEL extends DetectionResult

  sealed trait DetectionModelType
  final case object UpperBound extends DetectionModelType
  final case object MovingWindow extends DetectionModelType

  case class Event(eventId: String, sensorId: String, timestamp: Int, value: Double, modelType: DetectionModelType)

  case class SensorAssignment(sensorId: String, model: String, threshold: Double, modelParams: Option[Map[String, String]])
  case class ModelConfig(modelMapping: Seq[SensorAssignment])
}
