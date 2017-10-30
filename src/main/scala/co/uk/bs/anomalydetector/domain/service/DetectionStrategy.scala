package co.uk.bs.anomalydetector.domain.service

import co.uk.bs.anomalydetector.model.{ANOMALY, DetectionResult, EventStore, NO_ANOMALY}

trait DetectionStrategy {
  def evaluate(eventStore: EventStore, threshold: Double): DetectionResult

  def validateInput(state: EventStore, threshold: Double): Unit = {
    if (state.events.isEmpty) throw new IllegalArgumentException("The event store is empty.")
    if (threshold < 0) throw new IllegalArgumentException("The threshold cannot be negative.")
  }
}

class UpperBoundStrategy extends DetectionStrategy {
  override def evaluate(eventStore: EventStore, threshold: Double): DetectionResult = {
    validateInput(eventStore, threshold)
    eventStore.events.last.value match {
      case value if value > threshold => ANOMALY
      case _ => NO_ANOMALY
    }
  }
}

class MovingWindowStrategy(windowSize: Int) extends DetectionStrategy {
  override def evaluate(state: EventStore, threshold: Double): DetectionResult = {
    val mostRecentN = state.events.map(_.value).takeRight(windowSize)
    mostRecentN.sum / mostRecentN.size match {
      case avg if avg > threshold => ANOMALY
      case _ => NO_ANOMALY
    }
  }
}
