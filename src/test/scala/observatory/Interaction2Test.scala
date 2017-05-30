package observatory

import observatory.Interaction2._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class Interaction2Test extends FunSuite with Checkers {
  test("yearBound should return different value when layer is updated") {
    val signal = Var(availableLayers.head)
    assert(yearBounds(signal)().head == 1975)
    signal() = availableLayers.last
    assert(yearBounds(signal)().head == 1991)
  }
}
