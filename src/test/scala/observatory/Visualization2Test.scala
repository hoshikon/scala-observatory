package observatory

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class Visualization2Test extends FunSuite with Checkers {
  test("bilinearInterpolation should return the middle value if x and y are 0.5") {
    val center = Visualization2.bilinearInterpolation(0.5, 0.5, 0.0, 1.0, 0.0, 1.0)
    assert(center == 0.5)
  }

  test("bilinearInterpolation should return the 1/4 of the value") {
    val center = Visualization2.bilinearInterpolation(0.78, 0.25, 0.0, 1.0, 0.0, 1.0)
    assert(center == 0.25)
  }

}
