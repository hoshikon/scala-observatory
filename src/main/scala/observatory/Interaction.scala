package observatory

import java.lang.Math._

import com.sksamuel.scrimage.{Image, Pixel}
import observatory.Visualization._

/**
  * 3rd milestone: interactive visualization
  */
object Interaction {

  /**
    * @param zoom Zoom level
    * @param x X coordinate
    * @param y Y coordinate
    * @return The latitude and longitude of the top-left corner of the tile, as per http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
    */
  def tileLocation(zoom: Int, x: Int, y: Int): Location = {
    val numOfPartitions = pow(2, zoom)
    val lon = (360.0 * (x / numOfPartitions)) - 180.0
    val lat = atan(sinh(PI - (y / numOfPartitions) * 2 * PI)) * 180.0 / PI
    Location(lat, lon)
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @param zoom Zoom level
    * @param x X coordinate
    * @param y Y coordinate
    * @return A 256×256 image showing the contents of the tile defined by `x`, `y` and `zooms`
    */
  def tile(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)], zoom: Int, x: Int, y: Int): Image = {
    var progress = 0
    val pixels = for {
      row <- ((y * 256) until ((y + 1) * 256)).par
      col <- (x * 256) until ((x + 1) * 256)
    } yield {
      val location = tileLocation(zoom + 8, col, row)
      val c = interpolateColor(colors, predictTemperature(temperatures, location))

      if (col == (x + 1) * 256 - 1) {
        progress = progress + 1
        val length = progress * 30 / 256 // 0 to 10
        val arrow: String = (0 until length).map(n => "=").mkString + ">"
        val space = (0 until (30 - length)).map(n =>" ").mkString
        val bar = s"|${arrow + space}|"
        if (progress == 256) println(bar + " Done!")
        else print(bar + "\r")
      }

      Pixel(c.red, c.green, c.blue, 127)
    }
    Image(256, 256, pixels.toArray)
  }

  /**
    * Generates all the tiles for zoom levels 0 to 3 (included), for all the given years.
    * @param yearlyData Sequence of (year, data), where `data` is some data associated with
    *                   `year`. The type of `data` can be anything.
    * @param generateImage Function that generates an image given a year, a zoom level, the x and
    *                      y coordinates of the tile and the data to build the image from
    */
  def generateTiles[Data](
    yearlyData: Iterable[(Int, Data)],
    generateImage: (Int, Int, Int, Int, Data) => Unit
  ): Unit = {
    for {
      (year, data) <- yearlyData
      zoom <- 0 to 3
      x <- 0 until pow(2, zoom).toInt
      y <- 0 until pow(2, zoom).toInt
    } generateImage(year, zoom, x, y, data)
  }

}
