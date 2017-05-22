package observatory

import java.lang.Math._

import com.sksamuel.scrimage.Pixel
import observatory.Interaction._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

import scala.collection.concurrent.TrieMap

@RunWith(classOf[JUnitRunner])
class InteractionTest extends FunSuite with Checkers {
  test("tileLocation should return correct location") {
    assert(tileLocation(0, 0, 0) == Location(atan(sinh(PI)) * 180 / PI, -180.0))
  }

  test("tile should return a blank image") {
    val temperatures: Iterable[(Location, Double)] =
      for {
        lat <- Seq(-89, 90)
        lon <- Seq(-180, 179)
      } yield (Location(lat, lon), 0.0)
    val colors: Iterable[(Double, Color)] =
      Seq(
        (-10.0, Color(0, 0, 0)),
        (0.0, Color(0, 0, 0)),
        (10.0, Color(0, 0, 0))
      )
    val image = tile(temperatures, colors, 0, 0, 0)
    assert(image.pixels.forall(_ == Pixel(0, 0, 0, 127)))
  }
}
