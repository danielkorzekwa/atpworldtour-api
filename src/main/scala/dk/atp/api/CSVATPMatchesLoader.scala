package dk.atp.api

import ATPMatchesLoader._
import org.joda.time._
import org.apache.commons.io.FileUtils._
import java.io.File
import scala.collection.JavaConversions._
import java.text.SimpleDateFormat
import scala.io.Source
import dk.atp.api.tournament.TournamentAtpApi._
import dk.atp.api.AtpWorldTourApi.SurfaceEnum

object CSVATPMatchesLoader {

  private val DATA_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS"

  /**Writes matches data to csv file.*/
  def toCSVFile(matches: List[MatchComposite], matchesDataFile: String) {

    val df = new SimpleDateFormat(DATA_FORMAT)

    val fileOut = new File(matchesDataFile)
    val matchData: List[String] = matches.
      sortWith((a, b) => a.tournament.tournamentTime.getTime() < b.tournament.tournamentTime.getTime)
      .map(toCSVLine(_, df))

    val header = "event_time,event_name,surface,num_of_sets,playerA,playerB,winner,score,round,duration_minutes," +
      "playerATotalServicePointsWon,playerATotalServicePoints,playerBTotalServicePointsWon,playerBTotalServicePoints"
    writeLines(fileOut, header :: matchData)
  }

  def fromCSVFile(matchesDataFile: String): CSVATPMatchesLoader = {
    val df = new SimpleDateFormat(DATA_FORMAT)

    val matchesSource = Source.fromFile(matchesDataFile)
    val matches = matchesSource.getLines().drop(1).map(fromCSVLine(_, df)).toList
    new CSVATPMatchesLoader(matches)
  }

  private def toCSVLine(matchData: MatchComposite, df: SimpleDateFormat): String = {
    val tournament = matchData.tournament
    val tennisMatch = matchData.tennisMatch
    val matchFacts = matchData.matchFacts
    val playerAFacts = matchData.matchFacts.playerAFacts
    val playerBFacts = matchData.matchFacts.playerBFacts

    val csvRecord = df.format(tournament.tournamentTime) :: tournament.tournamentName.replaceAll(",", ";") :: tournament.surface ::
      tournament.numOfSet :: playerAFacts.playerName :: playerBFacts.playerName :: matchFacts.winner ::
      tennisMatch.score.replaceAll(",", ";") :: matchFacts.round :: matchFacts.durationMinutes :: playerAFacts.totalServicePointsWon :: playerAFacts.totalServicePoints ::
      playerBFacts.totalServicePointsWon :: playerBFacts.totalServicePoints :: Nil
    csvRecord.mkString(",")
  }

  private def fromCSVLine(matchDataCSV: String, df: SimpleDateFormat): MatchComposite = {

    val matchDataCSVArray = matchDataCSV.split(",")

    val tournamentTime = df.parse(matchDataCSVArray(0))
    val tournamentName = matchDataCSVArray(1)
    val surface = SurfaceEnum.fromText(matchDataCSVArray(2))
    val numOfSets = matchDataCSVArray(3)
    val tournament = Tournament(tournamentTime, tournamentName, surface, 2, "n/a")

    val score = matchDataCSVArray(7)
    val tennisMatch = Match(score, "n/a")

    val playerAFacts = PlayerFacts(matchDataCSVArray(4), matchDataCSVArray(10).toInt, matchDataCSVArray(11).toInt)
    val playerBFacts = PlayerFacts(matchDataCSVArray(5), matchDataCSVArray(12).toInt, matchDataCSVArray(13).toInt)
    val winner = matchDataCSVArray(6)
    val roud = matchDataCSVArray(8)
    val durationMinutes = matchDataCSVArray(9).toInt

    val matchFacts = MatchFacts(playerAFacts, playerBFacts, winner, roud, durationMinutes)

    MatchComposite(tournament, tennisMatch, matchFacts)
  }
}

class CSVATPMatchesLoader(matches: List[MatchComposite]) extends ATPMatchesLoader {
  def loadMatches(year: Int): List[MatchComposite] = matches.filter(m => new DateTime(m.tournament.tournamentTime).getYear() == year)
}