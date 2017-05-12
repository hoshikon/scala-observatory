package observatory

import java.lang.Math._

import com.sksamuel.scrimage.{Image, Pixel}

/**
  * 2nd milestone: basic visualization
  */
object Visualization {

  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location Location where to predict the temperature
    * @return The predicted temperature at `location`
    */
  def predictTemperature(temperatures: Iterable[(Location, Double)], location: Location): Double = {
    val RADUIS_EARTH = 6371

    def radians(degree: Double) = degree * PI / 180

    def greatCircleDistance(l1: Location, l2: Location) = {
      val lat1 = radians(l1.lat)
      val lat2 = radians(l2.lat)
      val lon1 = radians(l1.lon)
      val lon2 = radians(l2.lon)
      val deltaLon = abs(lon1 - lon2)
      val centralAngle = acos(sin(lat1) * sin(lat2) + cos(lat1) * cos(lat2) * cos(deltaLon))
      RADUIS_EARTH * centralAngle
    }

    def inverseDistanceWeight: Double = {
      val POWER_PARAM = 2.0
      def w(distance: Double) = 1 / pow(distance, POWER_PARAM)

      temperatures
      .map(temp => temp._2 * w(greatCircleDistance(temp._1, location)))
      .sum / temperatures.map(temp => w(greatCircleDistance(temp._1, location))).sum
    }

    temperatures.find(temp => greatCircleDistance(temp._1, location) < 1) match {
      case Some(temp) => temp._2
      case None => inverseDistanceWeight
    }
  }

  /**
    * @param points Pairs containing a value and its associated color
    * @param value The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Double, Color)], value: Double): Color = {
    val sortedPoints = points.toSeq.sortBy(_._1)
    if (value <= sortedPoints.head._1) sortedPoints.head._2
    else if (value >= sortedPoints.last._1) sortedPoints.last._2
    else {
      val index = sortedPoints.indexWhere(_._1 > value)
      val (c1, c2) = (sortedPoints(index - 1)._2, sortedPoints(index)._2)
      val (v1, v2) = (sortedPoints(index - 1)._1, sortedPoints(index)._1)
      val red = round(c1.red + (c2.red - c1.red) * (value - v1) / (v2 - v1)).toInt
      val green = round(c1.green + (c2.green - c1.green) * (value - v1) / (v2 - v1)).toInt
      val blue = round(c1.blue + (c2.blue - c1.blue) * (value - v1) / (v2 - v1)).toInt

      Color(red, green, blue)
    }
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)]): Image = {
    val array = new Array[Double](360 * 180)

    for {
      y <- (-89 to 90).reverse
      x <- -180 to 179
    } array((90 - y) * 360 + (180 + x)) = predictTemperature(temperatures, Location(y, x))

    val pixels = array
      .map(temp => {
        val c = interpolateColor(colors, temp)
        Pixel(c.red, c.green, c.blue, 127)
      })

    Image(360, 180, pixels)
  }

}
