package co.uk.bs.anomalydetector.infastructure.port.http

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.testkit.TestProbe
import co.uk.bs.anomalydetector.RouteTestBase
import co.uk.bs.anomalydetector.util.Sys
import co.uk.bs.dto.{DetectionResultDto, EventDto}
import org.scalatest.concurrent.Eventually

class EventRouteSpec extends RouteTestBase with Eventually {

  private val actorProbe = TestProbe()

  trait FakeSys extends Sys {
    override val routerActor = actorProbe.ref
  }

  private val eventRoute = new EventRoute with MessageRouterFactorySlice with Sys with FakeSys

  "Event api" should {
    "delegate to the service layer and return the response" in {
      val request =
        """
          |{
          |    "eventId" : "cj86g5ypk000004zvevipqxfn",
          |    "sensorId" : "fd0a635d-2aaf-4460-a817-6353e1b6c050",
          |    "timestamp" : 1506723249,
          |    "value" : 25.6734
          |}
        """.stripMargin

      val expectedResponse = DetectionResultDto(
        eventId = "cj86g5ypk000004zvevipqxfn",
        sensorId = "fd0a635d-2aaf-4460-a817-6353e1b6c050",
        timestamp = 1506723249,
        value = 25.6734,
        status = "",
        cause = "",
        message = ""
      )

      Post("/event", HttpEntity(ContentTypes.`application/json`, request)) ~> eventRoute.eventRoute ~> check {
        actorProbe.expectMsg(EventDto("cj86g5ypk000004zvevipqxfn", "fd0a635d-2aaf-4460-a817-6353e1b6c050", 1506723249, 25.6734))
        actorProbe.reply(expectedResponse)
        eventually {
          status shouldEqual StatusCodes.OK
          contentType should be(ContentTypes.`application/json`)
          responseAs[DetectionResultDto] shouldBe expectedResponse
        }
      }
    }
  }
}
