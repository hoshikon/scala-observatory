package observatory

import java.io.File

object Main extends App {
  val year = 2010
  println("Loading Data from file.")
  val dataOfYear = Extraction.locateTemperatures(year, "/stations.csv", s"/$year.csv")
  val yearlyAvg: Iterable[(Location, Double)] = Extraction.locationYearlyAverageRecords(dataOfYear)

  println("Data loading finished!")

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

  println("Creating Image..")
  val Image = Visualization.visualize(yearlyAvg, colorTemp)
  println("Image Created!")
  val zoom = 0
  val (x, y) = (0, 0)
  println("Writing on file...")
  val path = s"target/temperatures/$year/$zoom/$x-$y.png"
  Image.output(new File(path))
  println("Writing Finished!!")
}

//object seeIt extends App {
//  for {
//    year <- 1991 to 2015
//    zoom <- 0 to 3
////    x <- 0 until Math.pow(2, zoom).toInt
////    y <- 0 until Math.pow(2, zoom).toInt
//  } {
//    val f = new File(s"target/deviations/$year/$zoom")
//    if (!f.exists()) {
//      print(s"${f.getAbsolutePath} doesn't exits   ")
//      if (f.mkdirs()) println("created :)") else println("not created :(")
//    }
//  }
//}
