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

  feature("Upper bound threshold anomaly detector") {

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

  feature("Moving window avg threshold anomaly detector") {

    scenario("NO_ANOMALY gets returned with window size 5")  {

      val expectedResponse = DetectionResultDto(
        eventId = "event6",
        sensorId = "863beb58-d820-4512-888e-6383bbe1ef77",
        timestamp = 1506723249,
        value = 33.0,
        status = "NO_ANOMALY",
        cause = "",
        message = "")

      Given("I post 6 events")
      postEvent(createRequest("event1", "863beb58-d820-4512-888e-6383bbe1ef77", 999.0))
      postEvent(createRequest("event2", "863beb58-d820-4512-888e-6383bbe1ef77", 33.0))
      postEvent(createRequest("event3", "863beb58-d820-4512-888e-6383bbe1ef77", 33.0))
      postEvent(createRequest("event4", "863beb58-d820-4512-888e-6383bbe1ef77", 33.0))
      postEvent(createRequest("event5", "863beb58-d820-4512-888e-6383bbe1ef77", 33.0))

      Then("The status of the least response is NO_ANOMALY")
      postEvent(createRequest("event6", "863beb58-d820-4512-888e-6383bbe1ef77", 33.0), expectedResponse)
    }

    scenario("ANOMALY gets returned with window size 5")  {

      val expectedResponse = DetectionResultDto(
        eventId = "event6",
        sensorId = "863beb58-d820-4512-888e-6383bbe1ef77",
        timestamp = 1506723249,
        value = 200.0,
        status = "ANOMALY",
        cause = "",
        message = "")

      Given("I post 6 events")
      postEvent(createRequest("event1", "863beb58-d820-4512-888e-6383bbe1ef77", 10.0))
      postEvent(createRequest("event2", "863beb58-d820-4512-888e-6383bbe1ef77", 10.0))
      postEvent(createRequest("event3", "863beb58-d820-4512-888e-6383bbe1ef77", 10.0))
      postEvent(createRequest("event4", "863beb58-d820-4512-888e-6383bbe1ef77", 10.0))
      postEvent(createRequest("event5", "863beb58-d820-4512-888e-6383bbe1ef77", 90.0))

      Then("The status of the least response is ANOMALY")
      postEvent(createRequest("event6", "863beb58-d820-4512-888e-6383bbe1ef77", 200.0), expectedResponse)
    }

    scenario("Different sensor states are stored in different actors")  {
      val sensor1expectedResponse = DetectionResultDto(
        eventId = "event3",
        sensorId = "b46dfc91-7a62-4fcc-966a-862dcb053af3",
        timestamp = 1506723249,
        value = 20.0,
        status = "NO_ANOMALY",
        cause = "",
        message = "")

      val sensor2expectedResponse = DetectionResultDto(
        eventId = "event4",
        sensorId = "32db86fa-e853-4080-94d4-d6125ee028b3",
        timestamp = 1506723249,
        value = 90,
        status = "ANOMALY",
        cause = "",
        message = "")

      postEvent(createRequest("event1", "b46dfc91-7a62-4fcc-966a-862dcb053af3", 10.0))
      postEvent(createRequest("event2", "32db86fa-e853-4080-94d4-d6125ee028b3", 80.0))
      postEvent(createRequest("event3", "b46dfc91-7a62-4fcc-966a-862dcb053af3", 20.0), sensor1expectedResponse)
      postEvent(createRequest("event4", "32db86fa-e853-4080-94d4-d6125ee028b3", 90.0), sensor2expectedResponse)
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

  private def postEvent(request: EventDto) = {
    Post("/event", HttpEntity(ContentTypes.`application/json`, write(request))) ~> eventRoute.eventRoute ~> check {
      eventually {
        status shouldEqual StatusCodes.OK
        contentType should be(ContentTypes.`application/json`)
      }
    }
  }
}
