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

      val playerAFacts = PlayerFacts(matchDataCSVArray(4), matchDataCSVArray(10).toInt, matchDataCSVArray(11).toInt)
      val playerBFacts = PlayerFacts(matchDataCSVArray(5), matchDataCSVArray(12).toInt, matchDataCSVArray(13).toInt)
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

      val csvRecord = df.format(tournament.tournamentTime) :: tournament.tournamentName.replaceAll(",", ";") :: tournament.surface ::
        tournament.numOfSet :: playerAFacts.playerName :: playerBFacts.playerName :: matchFacts.winner ::
        tennisMatch.score.replaceAll(",", ";") :: matchFacts.round :: matchFacts.durationMinutes :: playerAFacts.totalServicePointsWon :: playerAFacts.totalServicePoints ::
        playerBFacts.totalServicePointsWon :: playerBFacts.totalServicePoints :: Nil
      csvRecord.mkString(",")
    }
  }