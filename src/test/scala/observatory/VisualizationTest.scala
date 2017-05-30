package observatory


import observatory.Visualization.{interpolateColor, predictTemperature}
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class VisualizationTest extends FunSuite with Checkers {

  test("predictTemperature should guess temperature from given dataset") {
    val dataset: Iterable[(Location, Double)] =
      Seq(
        (Location(0.0, 0.0), 0.0),
        (Location(1.0, 0.0), 0.0),
        (Location(0.0, 1.0), 10.0),
        (Location(1.0, 1.0), 10.0)
      )
    val result = predictTemperature(dataset, Location(0.5, 0.5))
    assert(result == 5.0)
  }

  test("interpolateColor should return correct color") {
    val colors: Iterable[(Double, Color)] =
      Seq(
        (0.0, Color(0, 0, 0)),
        (5.0, Color(50, 0, 0)),
        (10.0, Color(150, 0, 0))
      )
    val result = interpolateColor(colors, 7.5)
    assert(result.red == 100)
  }

}
