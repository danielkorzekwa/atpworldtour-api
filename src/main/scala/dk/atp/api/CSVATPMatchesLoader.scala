package dk.atp.api

import ATPMatchesLoader._
import org.joda.time._
import org.apache.commons.io.FileUtils._
import java.io.File
import scala.collection.JavaConversions._
import java.text.SimpleDateFormat

object CSVATPMatchesLoader {

  private val DATA_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS"

  /**Writes matches data to csv file.*/
  def toCSVFile(matches: List[MatchComposite], matchesDataFile: String) {

    val df = new SimpleDateFormat(DATA_FORMAT)

    val fileOut = new File(matchesDataFile)
    val matchData: List[String] = matches.
      sortWith((a, b) => a.tournament.tournamentTime.getTime() < b.tournament.tournamentTime.getTime)
      .map(toCSVFile(_, df))

    val header = "event_time,event_name,surface,num_of_sets,playerA,playerB,winner,score,round,duration_minutes," +
      "playerATotalServicePointsWon,playerATotalServicePoints,playerBTotalServicePointsWon,playerBTotalServicePoints"
    writeLines(fileOut, header :: matchData)
  }

  private def toCSVFile(matchData: MatchComposite, df: SimpleDateFormat): String = {
    val tournament = matchData.tournament
    val tennisMatch = matchData.tennisMatch
    val matchFacts = matchData.matchFacts
    val playerAFacts = matchData.matchFacts.playerAFacts
    val playerBFacts = matchData.matchFacts.playerBFacts

    val csvRecord = df.format(tournament.tournamentTime) :: tournament.tournamentName :: tournament.surface ::
      tournament.numOfSet :: playerAFacts.playerName :: playerBFacts.playerName :: matchFacts.winner ::
      tennisMatch.score.replaceAll(",", ";") :: matchFacts.durationMinutes :: playerAFacts.totalServicePointsWon :: playerAFacts.totalServicePoints ::
      playerBFacts.totalServicePointsWon :: playerBFacts.totalServicePoints :: Nil
    csvRecord.mkString(",")
  }
}

class CSVATPMatchesLoader(matches: List[MatchComposite]) extends ATPMatchesLoader {
  def loadMatches(year: Int): List[MatchComposite] = matches.filter(m => new DateTime(m.tournament.tournamentTime).getYear() == year)
}