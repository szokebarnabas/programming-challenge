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
}
