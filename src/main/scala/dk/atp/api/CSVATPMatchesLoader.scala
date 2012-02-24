package dk.atp.api

import domain._
import org.joda.time._
import org.apache.commons.io.FileUtils._
import java.io.File
import scala.collection.JavaConversions._
import java.text.SimpleDateFormat
import scala.io.Source
import dk.atp.api.tournament.TournamentAtpApi._
import dk.atp.api.domain.SurfaceEnum._

object CSVATPMatchesLoader {

  private val DATA_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS"

  /**Writes matches data to csv file.*/
  def toCSVFile(matches: List[MatchComposite], matchesDataFile: String) {

    val df = new SimpleDateFormat(DATA_FORMAT)

    val fileOut = new File(matchesDataFile)
    val matchData: List[String] = matches.
      sortWith((a, b) => a.tournament.tournamentTime.getTime() < b.tournament.tournamentTime.getTime)
      .map(_.toCSVLine(df))

    val header = "event_time,event_name,surface,num_of_sets,playerA,playerB,winner,score,round,duration_minutes," +
      "playerATotalServicePointsWon,playerATotalServicePoints,playerBTotalServicePointsWon,playerBTotalServicePoints"
    writeLines(fileOut, header :: matchData)
  }

  def fromCSVFile(matchesDataFile: String): CSVATPMatchesLoader = {
    val df = new SimpleDateFormat(DATA_FORMAT)

    val matchesSource = Source.fromFile(matchesDataFile)
    val matches = matchesSource.getLines().drop(1).map(MatchComposite.fromCSVLine(_, df)).toList
    new CSVATPMatchesLoader(matches)
  }

}

class CSVATPMatchesLoader(matches: List[MatchComposite]) extends ATPMatchesLoader {
  def loadMatches(year: Int): List[MatchComposite] = matches.filter(m => new DateTime(m.tournament.tournamentTime).getYear() == year)
}