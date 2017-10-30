package co.uk.bs.anomalydetector.infastructure.port.http

import akka.pattern.ask
import co.uk.bs.anomalydetector.domain.service.MessageRouterFactorySlice
import co.uk.bs.anomalydetector.util.{HttpResponseHelper, JsonSupport, Sys}
import co.uk.bs.dto.{DetectionResultDto, EventDto}

trait EventRoute extends JsonSupport with HttpResponseHelper {
  this: MessageRouterFactorySlice with Sys =>

  import akka.http.scaladsl.server.Directives._
  import akka.http.scaladsl.server.ExceptionHandler

  val exceptionHandler = ExceptionHandler {
    case _ => internalServerError("Failed to ingest event.")
  }

  val eventRoute = pathPrefix("event") {
    handleExceptions(exceptionHandler) {
      pathEndOrSingleSlash {
        post {
          entity(as[EventDto]) { dto =>
            onSuccess((routerActor ? dto).mapTo[DetectionResultDto]) { detectionResultDto =>
              ok(detectionResultDto)
            }
          }
        }
      }
    }
  }
}
