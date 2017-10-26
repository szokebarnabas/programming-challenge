package co.uk.bs.anomalydetector

import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{DefaultFormats, jackson}

trait JsonSupport extends Json4sSupport {

  implicit val serialization = jackson.Serialization
  implicit val formats = DefaultFormats


}
