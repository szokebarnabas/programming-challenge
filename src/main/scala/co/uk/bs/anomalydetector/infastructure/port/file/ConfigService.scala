package co.uk.bs.anomalydetector.infastructure.port.file

import co.uk.bs.anomalydetector.model.ModelConfig
import co.uk.bs.anomalydetector.util.JsonSupport
import org.slf4j.LoggerFactory

import scala.io.Source
import scala.util.{Failure, Success, Try}

class ConfigService extends JsonSupport {

  import org.json4s.jackson.Serialization.read

  def readConfig(path: String): Either[String, ModelConfig] = {
    Try {
      val content = Source.fromFile(path).mkString
      read[ModelConfig](content)
    } match {
      case Success(config) => Right(config)
      case Failure(t) =>
        LoggerFactory.getLogger(getClass).error("Failed to read config file.", t)
        Left("Failed to read config file.")
    }
  }
}
