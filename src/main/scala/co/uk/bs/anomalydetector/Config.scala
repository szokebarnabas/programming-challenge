package co.uk.bs.anomalydetector

import com.typesafe.config.ConfigFactory

trait Config {

  val conf = ConfigFactory.load()

  private val httpConfig = conf.getConfig("http")
  val httpHost = httpConfig.getString("host")
  val httpPort = httpConfig.getInt("port")

  val modelConfigPath = conf.getString("model-config-path")
}
