package co.uk.bs.anomalydetector

import akka.http.scaladsl.testkit.ScalatestRouteTest
import co.uk.bs.anomalydetector.util.JsonSupport
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

abstract class BaseTest extends WordSpec with Matchers with BeforeAndAfterAll with ScalaFutures

abstract class RouteTestBase extends BaseTest with ScalatestRouteTest with JsonSupport