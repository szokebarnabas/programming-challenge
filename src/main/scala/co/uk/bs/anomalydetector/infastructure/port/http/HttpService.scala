package co.uk.bs.anomalydetector.infastructure.port.http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import co.uk.bs.anomalydetector.Config
import co.uk.bs.anomalydetector.domain.service.MessageRouterFactorySlice
import co.uk.bs.anomalydetector.util.Sys

trait HttpService {
  def routes: Route
}

trait HttpServiceSlice {

  val httpService = new HttpService with EventRoute with MessageRouterFactorySlice with Sys with Config {

    override def routes = {
      pathPrefix("api") {
        eventRoute
      }
    }
  }
}