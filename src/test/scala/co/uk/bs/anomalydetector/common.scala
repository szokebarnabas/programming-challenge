package co.uk.bs.anomalydetector

import akka.http.scaladsl.testkit.ScalatestRouteTest
import co.uk.bs.anomalydetector.util.JsonSupport
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

trait BaseTest extends WordSpecLike with Matchers with BeforeAndAfterAll with ScalaFutures with MockitoSugar

abstract class RouteTestBase extends BaseTest with ScalatestRouteTest with JsonSupport