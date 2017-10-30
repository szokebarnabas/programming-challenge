package co.uk.bs.anomalydetector.infastructure.port.file

import co.uk.bs.anomalydetector.{BaseTest, TestFixtures}

class ConfigServiceSpec extends BaseTest {

  private val configServiceSlice = new ConfigService()

  "Config service" should {
    "successfully parse config file" in {
      val path = getClass.getResource("/simpleConfig.json").getPath

      val result = configServiceSlice.readConfig(path)

      result shouldBe (Right(TestFixtures.expectedConfig))
    }

    "return the error when the file cannot be read" in {
      val result = configServiceSlice.readConfig("banana.json")

      result shouldBe (Left("Failed to read config file."))
    }
  }

}
