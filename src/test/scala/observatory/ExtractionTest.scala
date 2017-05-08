package observatory

import java.time.LocalDate

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ExtractionTest extends FunSuite {
  test("locateTemperatures should read 3 temperatures from one file") {
    val stationsFile = "src/test/resources/teststations.csv"
    val temparaturesFile = "src/test/resources/1986.csv"
    val result = Extraction
      .locateTemperatures(1986, stationsFile, temparaturesFile)
      .toSeq
      .sortBy(_._1.toEpochDay)

    assert(result.size == 3)
    assert(result.head._2 == Location(+70.933,-008.667))
    assert(result(0)._3 == 17.2)
    assert(result(1)._3 == 12.1)
    assert(result(2)._3 == 10.4)
  }
  
}