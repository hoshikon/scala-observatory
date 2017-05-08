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
      val centralAngle = acos(sin(lat1) * sin(lat2) + cos(lon1) * cos(lon2) * cos(deltaLon))
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
    val index = sortedPoints.indexWhere(_._1 >= value)
    val (colour1, colour2) = (sortedPoints(index - 1)._2, sortedPoints(index)._2)
    val red = (colour1.red + colour2.red) / 2
    val green = (colour1.green + colour2.green) / 2
    val blue = (colour1.blue + colour2.blue) / 2

    Color(red, green, blue)
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)]): Image = {
    ???
  }

}

