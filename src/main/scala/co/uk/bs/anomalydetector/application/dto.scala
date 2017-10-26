package co.uk.bs

package object dto {
  case class EventDto(eventId: String, sensorId: String, timestamp: Long, value: Double)
  case class DetectionResultDto(eventId: String,
                                sensorId: String,
                                timestamp: Long,
                                value: Double,
                                status: String,
                                cause: String,
                                message: String)
}