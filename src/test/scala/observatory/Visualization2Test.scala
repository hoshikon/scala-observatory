package observatory

import com.sksamuel.scrimage.Pixel
import observatory.Visualization2._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class Visualization2Test extends FunSuite with Checkers {
  test("bilinearInterpolation should return the middle value if x and y are 0.5") {
    val center = bilinearInterpolation(0.5, 0.5, 0.0, 1.0, 0.0, 1.0)
    assert(center == 0.5)
  }

  test("bilinearInterpolation should return the 1/4 of the value") {
    val center = bilinearInterpolation(0.78, 0.25, 0.0, 1.0, 0.0, 1.0)
    assert(center == 0.25)
  }

  test("visualizeGrid should return blank image") {
    val colors: Iterable[(Double, Color)] =
      Seq(
        (-10.0, Color(0, 0, 0)),
        (0.0, Color(0, 0, 0)),
        (10.0, Color(0, 0, 0))
      )
    val image = visualizeGrid({ case (x: Int, y: Int) => 0.0}, colors, 0, 0, 0)
    assert(image.pixels.forall(p => p == Pixel(0, 0, 0, 127)))
  }

}
