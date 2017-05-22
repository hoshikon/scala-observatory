package observatory

import java.io.File

import observatory.Interaction._
import observatory.Visualization2._
import observatory.Manipulation._

object ImageGenerator {
  def generateTempImages(yearlyData: Iterable[(Int, Iterable[(Location, Double)])], colors: Iterable[(Double, Color)]): Unit = {
    println("[Creating Images (Temperatures)]")
    generateTiles[Iterable[(Location, Double)]](
      yearlyData,
      (year, zoom, x, y, data) => {
        println(s"$year $zoom $x $y")
        val path = s"target/temperatures/$year/$zoom"
        val file = new File(s"$path/$x-$y.png")
        if (!file.exists()) {
          findOrCreateDirectory(path)
          val image = tile(data, colors, zoom, x, y)
          image.output(file)
        }
      })
    println("Images Created for Temperatures\n")

  }

  def generateDevImages(yearlyData: Iterable[(Int, Iterable[(Location, Double)])], colors: Iterable[(Double, Color)]) = {
    println("[Creating Images (Deviations)]")
    generateTiles[Iterable[(Location, Double)]](
      yearlyData.dropWhile(_._1 < 1990),
      (year, zoom, x, y, data) => {
        println(s"$year $zoom $x $y")
        val path = s"target/deviations/$year/$zoom"
        val file = new File(s"$path/$x-$y.png")
        if (!file.exists()) {
          findOrCreateDirectory(path)
          val image = visualizeGrid(
            deviation(data, average(yearlyData.takeWhile(_._1 < 1990).map(_._2))),
            colors, zoom, x, y)
          image.output(file)
        }
      })
    println("Image Created for Deviations\n")
  }

  private def findOrCreateDirectory(path: String): Unit = {
    val dir = new File(path)
    if (!dir.exists()) {
      if (dir.mkdirs()) println(s"created directory: $path") else println(s"could NOT create directory: $path")
    }
  }
}
