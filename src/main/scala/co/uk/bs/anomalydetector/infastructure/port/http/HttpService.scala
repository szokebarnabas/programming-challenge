package co.uk.bs.anomalydetector.infastructure.port.http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import co.uk.bs.anomalydetector.{Config, Sys}

trait HttpService {
  def routes: Route
}

trait HttpServiceSlice {

  val httpService = new HttpService with Sys with Config {

    override def routes = {
      pathPrefix("api") {
        ???
      }
    }
  }
}