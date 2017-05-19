package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import observatory.Interaction.tileLocation

/**
  * 5th milestone: value-added information visualization
  */
object Visualization2 {

  /**
    * @param x X coordinate between 0 and 1
    * @param y Y coordinate between 0 and 1
    * @param d00 Top-left value
    * @param d01 Bottom-left value
    * @param d10 Top-right value
    * @param d11 Bottom-right value
    * @return A guess of the value at (x, y) based on the four known values, using bilinear interpolation
    *         See https://en.wikipedia.org/wiki/Bilinear_interpolation#Unit_Square
    */
  def bilinearInterpolation(
    x: Double,
    y: Double,
    d00: Double,
    d01: Double,
    d10: Double,
    d11: Double
  ): Double = {
    (1 - y) * ((1 - x) * d00 + x * d10) + y * ((1 - x) * d01 + x * d11)
  }

  /**
    * @param grid Grid to visualize
    * @param colors Color scale to use
    * @param zoom Zoom level of the tile to visualize
    * @param x X value of the tile to visualize
    * @param y Y value of the tile to visualize
    * @return The image of the tile at (x, y, zoom) showing the grid using the given color scale
    */
  def visualizeGrid(
    grid: (Int, Int) => Double,
    colors: Iterable[(Double, Color)],
    zoom: Int,
    x: Int,
    y: Int
  ): Image = {
    var progress = 0
    val pixels = for {
      row <- ((y * 256) until ((y + 1) * 256)).par
      col <- (x * 256) until ((x + 1) * 256)
    } yield {
      val loc = tileLocation(zoom + 8, col, row)
      val (x0, y0) = (loc.lon.floor.toInt, loc.lat.ceil.toInt)
      val (x1, y1) = (x0 + 1, y0 - 1)

      val temp = bilinearInterpolation(
        loc.lon - loc.lon.floor,
        loc.lat.ceil - loc.lat,
        grid(y0, x0),
        grid(y1, x0),
        grid(y0, x1),
        grid(y1, x1))

      val c = Visualization.interpolateColor(colors, temp)

      if (col == (x + 1) * 256 - 1) {
        progress = progress + 1
        val length = progress * 100 / 256 // 0 to 10
        val arrow: String = (0 until length).map(n => "=").mkString + ">"
        val space = (0 until (100 - length)).map(n =>" ").mkString
        val bar = s"|${arrow + space}|"
        if (progress == 256) println(bar + " Done!")
        else print(bar + "\r")
      }

      Pixel(c.red, c.green, c.blue, 127)
    }

    Image(256, 256, pixels.toArray)
  }

}
