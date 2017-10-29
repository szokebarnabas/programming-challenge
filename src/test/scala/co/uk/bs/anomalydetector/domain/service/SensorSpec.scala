package co.uk.bs.anomalydetector.domain.service

import akka.pattern.ask
import akka.testkit.TestActorRef
import co.uk.bs.anomalydetector.BaseTest
import co.uk.bs.anomalydetector.domain.service.Sensor.UpdateState
import co.uk.bs.anomalydetector.model.Event
import co.uk.bs.anomalydetector.util.Sys
import co.uk.bs.dto.DetectionResultDto

import scala.util.Success

class SensorSpec extends BaseTest with Sys {

  "Sensor actor" should {

    "return NO_ANOMALY when the value is less then the threshold in the upperBound model" in {
      val actorRef = TestActorRef(new Sensor)
      val event = Event(eventId = "ev1", sensorId = "1354978e-711f-4f26-bd96-6a6735064076", value = 15.0)
      val sensorRequest = UpdateState(state = event)

      val result = actorRef ? sensorRequest

      val Success(detectionResult: DetectionResultDto) = result.value.get
      detectionResult should have (
        'eventId ("ev1"),
        'sensorId ("1354978e-711f-4f26-bd96-6a6735064076"),
        'value (15.0),
        'status ("NO_ANOMALY"),
        'cause (""),
        'message ("")
      )
    }

    "return ANOMALY when the value is greater then the threshold in the upperBound model" in {
      val actorRef = TestActorRef(new Sensor)
      val event = Event(eventId = "ev1", sensorId = "1354978e-711f-4f26-bd96-6a6735064076", value = 90.0)
      val sensorRequest = UpdateState(state = event)

      val result = actorRef ? sensorRequest

      val Success(detectionResult: DetectionResultDto) = result.value.get
      detectionResult should have (
        'eventId ("ev1"),
        'sensorId ("1354978e-711f-4f26-bd96-6a6735064076"),
        'value (90.0),
        'status ("ANOMALY"),
        'cause (""),
        'message ("")
      )
    }

    "return NO_MODEL when the sensor is not assigned to the upperBound model" in {
      val actorRef = TestActorRef(new Sensor)
      val event = Event(eventId = "ev1", sensorId = "71700441-c5dc-4569-92bd-e338ceb8668b", value = 90.0)
      val sensorRequest = UpdateState(state = event)

      val result = actorRef ? sensorRequest

      val Success(detectionResult: DetectionResultDto) = result.value.get
      detectionResult should have (
        'eventId ("ev1"),
        'sensorId ("71700441-c5dc-4569-92bd-e338ceb8668b"),
        'value (90.0),
        'status ("NO_MODEL"),
        'cause ("Model not found of sensor"),
        'message ("Model not found of sensor")
      )
    }

    "return ERROR when the threshold is negative" in {
      val actorRef = TestActorRef(new Sensor)
      val event = Event(eventId = "ev1", sensorId = "946b5321-43bf-4873-ae7a-39c54ef69692", value = -90.0)
      val sensorRequest = UpdateState(state = event)

      val result = actorRef ? sensorRequest

      val Success(detectionResult: DetectionResultDto) = result.value.get
      detectionResult should have (
        'eventId ("ev1"),
        'sensorId ("946b5321-43bf-4873-ae7a-39c54ef69692"),
        'value (-90.0),
        'status ("ERROR"),
        'cause ("The threshold cannot be negative."),
        'message ("Failed to evaluate sensor data.")
      )
    }
  }
}

