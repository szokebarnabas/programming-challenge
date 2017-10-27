package co.uk.bs

package object dto {

  case class ApiErrorDto(msg: String)

  case class EventDto(eventId: String, sensorId: String, timestamp: Int, value: Double)
  case class DetectionResultDto(eventId: String,
                                sensorId: String,
                                timestamp: Long,
                                value: Double,
                                status: String,
                                cause: String,
                                message: String)
}