package observatory

import java.lang.Math._

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

import scala.collection.concurrent.TrieMap

@RunWith(classOf[JUnitRunner])
class InteractionTest extends FunSuite with Checkers {
  test("tileLocation") {
    assert(Interaction.tileLocation(0, 0, 0) == Location(atan(sinh(PI)) * 180 / PI, -180.0))
  }
}
