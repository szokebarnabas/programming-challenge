package co.uk.bs.anomalydetector.util

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives.complete
import co.uk.bs.dto.ApiErrorDto

trait HttpResponseHelper extends JsonSupport {

  import akka.http.scaladsl.server.StandardRoute
  import org.json4s.jackson.Serialization.write

  private def completeWithStatus[T <: AnyRef](statusCode: StatusCode, response: T) = {
    complete(HttpResponse(
      status = statusCode,
      entity = HttpEntity(ContentTypes.`application/json`, write(response))))
  }

  def internalServerError[T <: AnyRef](response: T): StandardRoute = completeWithStatus(InternalServerError, response)

  def internalServerError(message: String): StandardRoute = internalServerError(ApiErrorDto(msg = message))

  def ok[T <: AnyRef](response: T) = completeWithStatus(OK, response)
}