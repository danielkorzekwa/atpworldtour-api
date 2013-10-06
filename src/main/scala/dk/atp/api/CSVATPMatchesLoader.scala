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
      // first player stats
      "a_aces,a_double_faults,a_first_serve_hits,a_first_serve_total,a_first_serve_won,a_first_serve_played,a_second_serve_won,a_second_serve_played," +
      "a_break_points_saved,a_break_points_total,a_service_games,a_service_points_won,a_service_points_total," +
      // second player stats
      "b_aces,b_double_faults,b_first_serve_hits,b_first_serve_total,b_first_serve_won,b_first_serve_played,b_second_serve_won,b_second_serve_played," +
      "b_break_points_saved,b_break_points_total,b_service_games,b_service_points_won,b_service_points_total"

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