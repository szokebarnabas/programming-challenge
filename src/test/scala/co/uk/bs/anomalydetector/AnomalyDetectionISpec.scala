package co.uk.bs.anomalydetector

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import co.uk.bs.anomalydetector.infastructure.port.http.{EventRoute, MessageRouterFactorySlice}
import co.uk.bs.anomalydetector.util.{JsonSupport, Sys}
import co.uk.bs.dto.{DetectionResultDto, EventDto}
import org.json4s.jackson.Serialization.write
import org.scalatest.concurrent.Eventually
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

class AnomalyDetectionISpec extends FeatureSpec with GivenWhenThen with Matchers with Eventually with ScalatestRouteTest with JsonSupport {

  private val eventRoute = new EventRoute with MessageRouterFactorySlice with Sys

  feature("Upper Bound Threshold Anomaly Detector") {

    scenario("ANOMALY gets returned")  {

      val expectedResponse = DetectionResultDto(
        eventId = "event1",
        sensorId = "1354978e-711f-4f26-bd96-6a6735064076",
        timestamp = 1506723249,
        value = 90.0,
        status = "ANOMALY",
        cause = "",
        message = "")

      Given("An event request with value above the threshold")
      val request = createRequest("event1", "1354978e-711f-4f26-bd96-6a6735064076", 90.0)

      Then("Returns anomaly")
      postEvent(request, expectedResponse)
    }

    scenario("NO_ANOMALY gets returned")  {

      val expectedResponse = DetectionResultDto(
        eventId = "event1",
        sensorId = "1354978e-711f-4f26-bd96-6a6735064076",
        timestamp = 1506723249,
        value = 27.0,
        status = "NO_ANOMALY",
        cause = "",
        message = "")

      Given("An event request with value above the threshold")
      val request = createRequest("event1", "1354978e-711f-4f26-bd96-6a6735064076", 27.0)

      Then("Returns no anomaly")
      postEvent(request, expectedResponse)
    }

    scenario("NO_MODEL gets returned")  {

      val expectedResponse = DetectionResultDto(
        eventId = "event1",
        sensorId = "foo",
        timestamp = 1506723249,
        value = 27.0,
        status = "NO_MODEL",
        cause = "Model not found of sensor",
        message = "Model not found of sensor")

      Given("An event request without assigned model")
      val request = createRequest("event1", "foo", 27.0)

      Then("Returns no model status")
      postEvent(request, expectedResponse)
    }

    scenario("ERROR gets returned")  {

      val expectedResponse = DetectionResultDto(
        eventId = "event1",
        sensorId = "946b5321-43bf-4873-ae7a-39c54ef69692",
        timestamp = 1506723249,
        value = 7.0,
        status = "ERROR",
        cause = "The threshold cannot be negative.",
        message = "Failed to evaluate sensor data.")

      Given("An event request without assigned model")
      val request = createRequest("event1", "946b5321-43bf-4873-ae7a-39c54ef69692", 7.0)

      Then("Returns error status")
      postEvent(request, expectedResponse)
    }
  }

  private def createRequest(eventId: String, sensorId: String, value: Double): EventDto = {
    EventDto(eventId = eventId, sensorId = sensorId, timestamp = 1506723249, value = value)
  }


  private def postEvent(request: EventDto, expected: DetectionResultDto) = {
    Post("/event", HttpEntity(ContentTypes.`application/json`, write(request))) ~> eventRoute.eventRoute ~> check {
      eventually {
        status shouldEqual StatusCodes.OK
        contentType should be(ContentTypes.`application/json`)
        responseAs[DetectionResultDto] should have (
          'eventId (expected.eventId),
          'sensorId (expected.sensorId),
          'value (expected.value),
          'status (expected.status),
          'cause (expected.cause),
          'message (expected.message),
        )
      }
    }
  }
}
