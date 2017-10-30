package co.uk.bs.anomalydetector.domain.service

import co.uk.bs.anomalydetector.BaseTest
import co.uk.bs.anomalydetector.model._
import org.scalatest.prop.TableDrivenPropertyChecks._

class UpperBoundStrategySpec extends BaseTest {

  private val sensorValues =
    Table(
      ("sensorValue", "threshold", "expectedState"),
      (10.0, 20.0, NO_ANOMALY),
      (90.0, 20.0, ANOMALY),
      (20.0, 20.0, NO_ANOMALY),
    )

  "Upper bound strategy" should {

    "return the correct detection result" in {
      forAll(sensorValues) { (sensorValue: Double, threshold: Double, expectedState: DetectionResult) =>
        val event = Event("1", "1", 0, sensorValue)
        val result = new UpperBoundStrategy().evaluate(EventStore(events = Seq(event)), threshold)
        result should be(expectedState)
      }
    }

    "return an exception when the event store is empty" in {
      intercept [IllegalArgumentException] {
        new UpperBoundStrategy().evaluate(EventStore(Seq()), 30.0)
      }
    }

    "return an the threshold is negative" in {
      intercept [IllegalArgumentException] {
        new UpperBoundStrategy().evaluate(EventStore(Seq()), -1)
      }
    }
  }
}
