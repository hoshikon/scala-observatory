package observatory

import java.time.LocalDate

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ExtractionTest extends FunSuite {
  test("locateTemperatures should read 3 temperatures from one file") {
    val stationsFile = "/teststations.csv"
    val temparaturesFile = "/test1986.csv"
    val result = Extraction
      .locateTemperatures(1986, stationsFile, temparaturesFile)
      .toSeq
      .sortBy(_._1.toEpochDay)

    def toCelsius(f: Double) = (f - 32.0) / 1.8

    assert(result.size == 3)
    assert(result.head._2 == Location(+70.933,-008.667))
    assert(result(0)._3 == toCelsius(17.2))
    assert(result(1)._3 == toCelsius(12.1))
    assert(result(2)._3 == toCelsius(10.4))
  }
  
}