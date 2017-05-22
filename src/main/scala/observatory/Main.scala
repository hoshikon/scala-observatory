package observatory

import observatory.Extraction.{locateTemperatures, locationYearlyAverageRecords}
import observatory.ImageGenerator.{generateDevImages, generateTempImages}

import scala.collection.immutable.Seq

object Main extends App {
  val colorTemp =
    Seq(
      (60.0, Color(255, 255, 255)),
      (32.0, Color(255, 0, 0)),
      (12.0, Color(255, 255, 0)),
      (0.0, Color(0, 255, 255)),
      (-15.0, Color(0, 0, 255)),
      (-27.0, Color(255, 0, 255)),
      (-50.0, Color(33, 0, 107)),
      (-60.0, Color(0, 0, 0))
    )

  val colorDev =
    Seq(
      (7.0, Color(0,0,0)),
      (4.0, Color(255, 0, 0)),
      (2.0, Color(255, 255,	0)),
      (0.0, Color(255, 255, 255)),
      (-2.0, Color(0,	255, 255)),
      (-7.0, Color(0, 0, 255))
    )

  println("===== START =====")
  println("[Loading Data]")
  val yearlyData = (1975 to 2015).map(year => {
    val dataOfYear = locateTemperatures(year, "/stations.csv", s"/$year.csv")
    val yearlyAvg: Iterable[(Location, Double)] = locationYearlyAverageRecords(dataOfYear)
    print(s"$year ")
    (year, yearlyAvg)
  })
  println("\nFinished Loading Data\n")

  generateTempImages(yearlyData, colorTemp)
  generateDevImages(yearlyData, colorDev)

  println("All Done :)")
}
