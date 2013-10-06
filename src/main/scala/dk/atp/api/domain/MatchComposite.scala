package dk.atp.api.domain

import dk.atp.api.tournament.TournamentAtpApi._
import dk.atp.api.domain.SurfaceEnum._
import java.text.SimpleDateFormat

object MatchComposite {
  def fromCSVLine(matchDataCSV: String, df: SimpleDateFormat): MatchComposite = {

    val matchDataCSVArray = matchDataCSV.split(",")

    val tournamentTime = df.parse(matchDataCSVArray(0))
    val tournamentName = matchDataCSVArray(1)
    val surface = SurfaceEnum.fromText(matchDataCSVArray(2))
    val numOfSets = matchDataCSVArray(3)
    val tournament = Tournament(tournamentTime, tournamentName, surface, numOfSets.toInt, "n/a")

    val score = matchDataCSVArray(7)
    val tennisMatch = Match(score, "n/a")

    val playerAFacts = PlayerFacts(matchDataCSVArray(4),
      matchDataCSVArray(10).toInt,
      matchDataCSVArray(11).toInt,
      matchDataCSVArray(12).toInt, matchDataCSVArray(13).toInt,
      matchDataCSVArray(14).toInt, matchDataCSVArray(15).toInt,
      matchDataCSVArray(16).toInt, matchDataCSVArray(17).toInt,
      matchDataCSVArray(18).toInt, matchDataCSVArray(19).toInt,
      matchDataCSVArray(20).toInt,
      matchDataCSVArray(21).toInt, matchDataCSVArray(22).toInt)

    val playerBFacts = PlayerFacts(matchDataCSVArray(5),
      matchDataCSVArray(23).toInt,
      matchDataCSVArray(24).toInt,
      matchDataCSVArray(25).toInt, matchDataCSVArray(26).toInt,
      matchDataCSVArray(27).toInt, matchDataCSVArray(28).toInt,
      matchDataCSVArray(29).toInt, matchDataCSVArray(30).toInt,
      matchDataCSVArray(31).toInt, matchDataCSVArray(32).toInt,
      matchDataCSVArray(33).toInt,
      matchDataCSVArray(34).toInt, matchDataCSVArray(35).toInt)

    val winner = matchDataCSVArray(6)
    val round = matchDataCSVArray(8)
    val durationMinutes = matchDataCSVArray(9).toInt

    val matchFacts = MatchFacts(playerAFacts, playerBFacts, winner, round, durationMinutes)

    MatchComposite(tournament, tennisMatch, matchFacts)
  }
}

case class MatchComposite(tournament: Tournament, tennisMatch: Match, matchFacts: MatchFacts) {

  def toCSVLine(df: SimpleDateFormat): String = {

    val playerAFacts = matchFacts.playerAFacts
    val playerBFacts = matchFacts.playerBFacts

    val csvRecord = List(
      df.format(tournament.tournamentTime), tournament.tournamentName.replaceAll(",", ";"), tournament.surface,
      tournament.numOfSet, playerAFacts.playerName, playerBFacts.playerName, matchFacts.winner,
      tennisMatch.score.replaceAll(",", ";"), matchFacts.round, matchFacts.durationMinutes,
      //first player stats
      playerAFacts.aces, playerAFacts.doubleFaults, playerAFacts.firstServeHits, playerAFacts.firstServeTotal, playerAFacts.firstServeWon, playerAFacts.firstServePlayed,
      playerAFacts.secondServeWon, playerAFacts.secondServePlayed, playerAFacts.breakPointsSaved, playerAFacts.breakPointsTotal, playerAFacts.serviceGames,
      playerAFacts.servicePointsWon, playerAFacts.servicePointsTotal,
      //second player stats
      playerBFacts.aces, playerBFacts.doubleFaults, playerBFacts.firstServeHits, playerBFacts.firstServeTotal, playerBFacts.firstServeWon, playerBFacts.firstServePlayed,
      playerBFacts.secondServeWon, playerBFacts.secondServePlayed, playerBFacts.breakPointsSaved, playerBFacts.breakPointsTotal, playerBFacts.serviceGames,
      playerBFacts.servicePointsWon, playerBFacts.servicePointsTotal)
    csvRecord.mkString(",")
  }
}