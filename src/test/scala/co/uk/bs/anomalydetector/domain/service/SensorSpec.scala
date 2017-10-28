package co.uk.bs.anomalydetector.domain.service

import akka.pattern.ask
import akka.testkit.TestActorRef
import co.uk.bs.anomalydetector.BaseTest
import co.uk.bs.anomalydetector.TestFixtures._
import co.uk.bs.anomalydetector.util.Sys
import co.uk.bs.dto.DetectionResultDto

import scala.util.Success

class SensorSpec extends BaseTest with Sys {

  "Sensor actor" should {
    "respond with a detection result" in {
      val actorRef = TestActorRef(new Sensor)

      val result = actorRef ? sensorRequest

      val Success(detectionResult: DetectionResultDto) = result.value.get
      detectionResult should have (
        'eventId (sensorResponse.eventId),
        'sensorId (sensorResponse.sensorId),
        'value (sensorResponse.value),
        'status (sensorResponse.status),
        'cause (sensorResponse.cause),
        'message (sensorResponse.message)
      )
    }
  }
}
