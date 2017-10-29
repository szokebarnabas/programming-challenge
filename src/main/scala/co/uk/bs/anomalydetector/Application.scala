package co.uk.bs.anomalydetector

import akka.http.scaladsl.Http
import co.uk.bs.anomalydetector.domain.service.ModelConfigStore
import co.uk.bs.anomalydetector.infastructure.port.http.HttpServiceSlice
import co.uk.bs.anomalydetector.util.Sys

import scala.util.{Failure, Success}

object Application extends App with HttpServiceSlice with Sys with Config {

  private lazy val log = createLogger

  ModelConfigStore.modelConfig

  log.info(s"Starting HTTP service on $httpHost:$httpPort")
  Http().bindAndHandle(httpService.routes, httpHost, httpPort) onComplete {
    case Success(_) => log.info(s"Service has been started.")
    case Failure(ex) => {
      log.error("Failed to bind HTTP service.  Shutting down", ex)
      system.terminate()
    }
  }
}
