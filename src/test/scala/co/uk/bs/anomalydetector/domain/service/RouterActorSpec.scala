package co.uk.bs.anomalydetector.domain.service

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import co.uk.bs.anomalydetector.domain.service.Sensor.UpdateState
import co.uk.bs.anomalydetector.model.{Event, UpperBound}
import org.scalatest.WordSpecLike

class RouterActorSpec extends TestKit(ActorSystem("MySpec")) with ImplicitSender with WordSpecLike {

  "Router actor" should {
    "create a new actor and delegate the message" in {
      val probe = TestProbe()
      val firstMsgOfActorA = UpdateState(state = Event(eventId="ev1", sensorId = "sensor1", timestamp = 123456, value = 10.5, modelType = UpperBound))
      val secondMsgOfActorA = UpdateState(state = Event(eventId="ev1", sensorId = "sensor1", timestamp = 123456, value = 10.5, modelType = UpperBound))

      val router = system.actorOf(Props(classOf[RouterActor]), "router")

      router ! firstMsgOfActorA

      //TODO assert
    }
  }
}
