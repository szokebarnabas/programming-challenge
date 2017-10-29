package co.uk.bs.anomalydetector.domain.service

import co.uk.bs.anomalydetector.Application.modelConfigPath
import co.uk.bs.anomalydetector.infastructure.port.file.ConfigService
import org.slf4j.LoggerFactory

object ModelConfigStore {

  lazy val modelConfig = new ConfigService().readConfig(modelConfigPath) match {
    case Left(error) =>
      LoggerFactory.getLogger(getClass).error(error)
      throw new IllegalArgumentException(error)
    case Right(modelConfig) => modelConfig
  }
}
