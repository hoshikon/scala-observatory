package observatory

import java.io.File

import observatory.Extraction.{locateTemperatures, locationYearlyAverageRecords}
import observatory.Interaction.{generateTiles, tile}
import observatory.Manipulation.{average, deviation}
import observatory.Visualization2.visualizeGrid

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
  val yearlyData = (1975 to 2015).map( year => {
    val dataOfYear = locateTemperatures(year, "/stations.csv", s"/$year.csv")
    val yearlyAvg: Iterable[(Location, Double)] = locationYearlyAverageRecords(dataOfYear)
    print(s"$year ")
    (year, yearlyAvg)
  })
  println("Loading Finished\n")
  println("[Creating Images (Temperatures)]")
  generateTiles(
    yearlyData,
    (year, zoom, x, y, data) => {
      val image = tile(data, colorTemp, zoom, x, y)
      image.output(new File(s"target/temperatures/$year/$zoom/$x-$y.png"))
      print(s"$year ")
    })
  println("Images Created for Temperatures\n")

  println("[Creating Images (Deviations)]")
  generateTiles(
    yearlyData.dropWhile(_._1 < 1991),
    (year, zoom, x, y, data) => {
      val image = visualizeGrid(
        deviation(data, average(yearlyData.map(_._2))),
        colorDev, zoom, x, y)
      image.output(new File(s"target/deviations/$year/$zoom/$x-$y.png"))
      print(s"$year ")
    })
  println("Image Created for Deviations\n")

  println("All Done :)")
}
