package dk.atp.api.domain

import java.text.SimpleDateFormat

case class TennisMatch(tournament: Tournament, player1: String, player2: String, score: String, winner: String, round: String, durationMinutes: Int, 
    p1Facts: PlayerFacts, p2Facts: PlayerFacts) {
  
   def toCSVLine(df: SimpleDateFormat): String = {

    val csvRecord = List(
      df.format(tournament.tournamentTime), tournament.tournamentName.replaceAll(",", ";"), tournament.surface,
      tournament.numOfSet,player1,player2, winner,
      score.replaceAll(",", ";"), round, durationMinutes,
      //first player stats
      p1Facts.aces, p1Facts.doubleFaults, p1Facts.firstServeHits, p1Facts.firstServeTotal, p1Facts.firstServeWon, p1Facts.firstServePlayed,
      p1Facts.secondServeWon, p1Facts.secondServePlayed, p1Facts.breakPointsSaved, p1Facts.breakPointsTotal, p1Facts.serviceGames,
      p1Facts.servicePointsWon, p1Facts.servicePointsTotal,
      //second player stats
      p2Facts.aces, p2Facts.doubleFaults, p2Facts.firstServeHits, p2Facts.firstServeTotal, p2Facts.firstServeWon, p2Facts.firstServePlayed,
      p2Facts.secondServeWon, p2Facts.secondServePlayed, p2Facts.breakPointsSaved, p2Facts.breakPointsTotal, p2Facts.serviceGames,
      p2Facts.servicePointsWon, p2Facts.servicePointsTotal)
    csvRecord.mkString(",")
  }
}

object TennisMatch {
  def fromCSVLine(matchDataCSV: String, df: SimpleDateFormat): TennisMatch = {

    val matchDataCSVArray = matchDataCSV.split(",")

    val tournamentTime = df.parse(matchDataCSVArray(0))
    val tournamentName = matchDataCSVArray(1)
    val surface = SurfaceEnum.fromText(matchDataCSVArray(2))
    val numOfSets = matchDataCSVArray(3)
    val tournament = Tournament(tournamentTime, tournamentName, surface, numOfSets.toInt)

    val score = matchDataCSVArray(7)

    val player1Name = matchDataCSVArray(4)
    val player2Name = matchDataCSVArray(5)

    val playerAFacts = PlayerFacts(
      matchDataCSVArray(10).toInt,
      matchDataCSVArray(11).toInt,
      matchDataCSVArray(12).toInt, matchDataCSVArray(13).toInt,
      matchDataCSVArray(14).toInt, matchDataCSVArray(15).toInt,
      matchDataCSVArray(16).toInt, matchDataCSVArray(17).toInt,
      matchDataCSVArray(18).toInt, matchDataCSVArray(19).toInt,
      matchDataCSVArray(20).toInt,
      matchDataCSVArray(21).toInt, matchDataCSVArray(22).toInt)

    val playerBFacts = PlayerFacts(
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

    val tennisMatch = TennisMatch(tournament, player1Name, player2Name, score, winner, round, durationMinutes, playerAFacts, playerBFacts)
    tennisMatch
  }
}