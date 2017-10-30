package co.uk.bs.anomalydetector.domain.service

import co.uk.bs.anomalydetector.BaseTest
import co.uk.bs.anomalydetector.model._

class MovingWindowStrategySpec extends BaseTest {

  "Moving window strategy" should {

    "returns ANOMALY when the avg of the 3 most recent events is greater then the threshold" in {
      val event1 = Event("event1", "1", 0, 10.0)
      val event2 = Event("event2", "1", 0, 20.0)
      val event3 = Event("event3", "1", 0, 80.0)
      val event4 = Event("event4", "1", 0, 80.0)
      val event5 = Event("event5", "1", 0, 90.0)

      val result = new MovingWindowStrategy(3).evaluate(EventStore(events =
        Seq(event1, event2, event3, event4, event5)), 83.33)

      result should be(ANOMALY)
    }

    "returns NO_ANOMALY when the avg of the 3 most recent events is less then the threshold" in {
      val event1 = Event("event1", "1", 0, 10.0)
      val event2 = Event("event2", "1", 0, 20.0)
      val event3 = Event("event3", "1", 0, 80.0)
      val event4 = Event("event4", "1", 0, 80.0)
      val event5 = Event("event5", "1", 0, 90.0)

      val result = new MovingWindowStrategy(3).evaluate(EventStore(events =
        Seq(event1, event2, event3, event4, event5)), 83.34)

      result should be(NO_ANOMALY)
    }

    "returns the correct status when the number of messages are less than the windows size" in {
      val event1 = Event("event1", "1", 0, 10.0)

      val result = new MovingWindowStrategy(99).evaluate(EventStore(events = Seq(event1)), 10.00)

      result should be(NO_ANOMALY)
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
