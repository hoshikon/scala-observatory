package observatory

import observatory.Manipulation._
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class ManipulationTest extends FunSuite with Checkers {
  val temps: Iterable[Iterable[(Location, Double)]] =
    Seq(
      Seq((Location(0.0, 0.0), 0.0)),
      Seq((Location(0.0, 0.0), 6.0)),
      Seq((Location(0.0, 0.0), 15.0))
    )
  test("average") {
    assert(average(temps)(0, 0) == 7.0)
  }

  test("deviation"){
    assert(deviation(temps.last, average(temps))(0, 0) == 8.0)
  }
}