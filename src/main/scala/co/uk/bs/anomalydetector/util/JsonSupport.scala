package co.uk.bs.anomalydetector.util

import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s._

trait JsonSupport extends Json4sSupport {

  implicit val serialization = jackson.Serialization
  implicit val formats = DefaultFormats


}
