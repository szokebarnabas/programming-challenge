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

  feature("Anomaly detection") {

    scenario("The detection result gets returned to the client")  {

      val expectedResponse = DetectionResultDto(
        eventId = "event1",
        sensorId = "1354978e-711f-4f26-bd96-6a6735064076",
        timestamp = 1506723249,
        value = 90.0,
        status = "ANOMALY",
        cause = "",
        message = "")

      Given("A senor event is sent")
      val request = createRequest("event1", "1354978e-711f-4f26-bd96-6a6735064076", 90.0)

      Then("The correct response is created")
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
