package observatory

import java.io.InputStream
import java.time.LocalDate

import scala.io.Source

/**
  * 1st milestone: data extraction
  */
object Extraction {

  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    */
  def locateTemperatures(year: Int, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Double)] = {
    val stationsInput: InputStream = getClass.getResourceAsStream(stationsFile)
    val tempInput: InputStream = getClass.getResourceAsStream(temperaturesFile)
    def readInput(inputStream: InputStream) = Source.fromInputStream(inputStream).getLines().map(_.split(",", -1).map(_.trim))
    def toCelsius(f: Double) = (f - 32.0) / 1.8
    val missing = toCelsius(9999.9)

    val stationToLocation = readInput(stationsInput)
      .filter(row => !row(2).isEmpty && !row(3).isEmpty)
      .map(row => (row(0), row(1)) -> Location(row(2).toDouble, row(3).toDouble))
      .toMap

    readInput(tempInput)
      .map(row => (
        LocalDate.of(year, row(2).toInt, row(3).toInt),
        stationToLocation.getOrElse((row(0), row(1)), null),
        toCelsius(row(4).toDouble)))
      .filter(tuple => tuple._2 != null && tuple._3 != missing)
      .toSeq
  }

  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Double)]): Iterable[(Location, Double)] = {
    records
      .groupBy(_._2)
      .mapValues(r => r.map(_._3).sum / r.size)
  }

}
