package co.uk.bs.anomalydetector.infastructure.port.http

import akka.actor.{ActorRef, Props}
import co.uk.bs.anomalydetector.domain.service.RouterActor
import co.uk.bs.anomalydetector.util.Sys

trait MessageRouterFactory {
  def createActor: ActorRef
}

trait MessageRouterFactorySlice {
  this: Sys =>

  val messageRouterFactory = new MessageRouterFactory {
    override def createActor: ActorRef = system.actorOf(Props[RouterActor])
  }
}
