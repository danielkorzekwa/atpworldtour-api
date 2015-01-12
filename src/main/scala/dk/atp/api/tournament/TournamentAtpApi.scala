package dk.atp.api.tournament

import TournamentAtpApi._
import java.util.Date
import dk.atp.api.domain.SurfaceEnum._
import dk.atp.api.domain.Tournament
import dk.atp.api.domain.Tournament
import dk.atp.api.domain.PlayerFacts

/**
 * Obtains all tennis tournaments from http://www.atpworldtour.com/ page for a given year.
 *
 * @author korzekwad
 */
object TournamentAtpApi {

  /**
   * @param numOfSet 2 (THREE_SET_MATCH) or 3 (FIVE_SET_MATCH)
   */
  case class APITournament(tournamentTime: Date, tournamentName: String, surface: SurfaceEnum, numOfSet: Int, tournamentUrl: String) {
    def toTournament():Tournament = Tournament(tournamentTime,tournamentName,surface,numOfSet)
  }
  
  sealed abstract class APIMatch(val score:String)
  case class APIMatch2014(override val score: String, matchFactsUrl: String) extends APIMatch(score)
  case class APIMatch2015(override val score:String,eventId:Int,year:Int,matchId:Int)  extends APIMatch(score)
  
  case class APIMatchFacts(playerAFacts: APIPlayerFacts, playerBFacts: APIPlayerFacts, winner: String, round: String, durationMinutes: Int) {

    def playerFacts(playerName: String): Option[PlayerFacts] = {
      val facts = playerName match {
        case playerAFacts.playerName => Option(playerAFacts.toPlayerFacts)
        case playerBFacts.playerName => Option(playerBFacts.toPlayerFacts)
        case _ => None
      }
      facts
    }

    def playerOpponentFacts(playerName: String): Option[PlayerFacts] = {
      val facts = playerName match {
        case playerAFacts.playerName => Option(playerBFacts.toPlayerFacts)
        case playerBFacts.playerName => Option(playerAFacts.toPlayerFacts)
        case _ => None
      }
      facts
    }

    def containsPlayer(playerName: String): Boolean = (playerAFacts.playerName :: playerBFacts.playerName :: Nil).contains(playerName)
  }

  case class APIPlayerFacts(playerName: String,
    aces: Int,
    doubleFaults: Int,
    firstServeHits: Int, firstServeTotal: Int,
    firstServeWon: Int, firstServePlayed: Int,
    secondServeWon: Int, secondServePlayed: Int,
    breakPointsSaved: Int, breakPointsTotal: Int,
    serviceGames: Int,
    servicePointsWon: Int,servicePointsTotal: Int) {
    def servicePointsWonPct: Double = servicePointsWon.toDouble / servicePointsTotal.toDouble
    def servicePointsLostPct = 1 - servicePointsWonPct
    
    def toPlayerFacts():PlayerFacts = {
      PlayerFacts(aces,doubleFaults,
          firstServeHits,firstServeTotal,
          firstServeWon,firstServePlayed,
          secondServeWon,secondServePlayed,
          breakPointsSaved,breakPointsTotal,
          serviceGames,
          servicePointsWon,servicePointsTotal)
    }
  }
}

trait TournamentAtpApi {

  /**
   * @param year
   */
  def parseTournaments(year: Int): List[APITournament]

  /**
   * @param tournamentUrl, e.g. http://www.atpworldtour.com/Share/Event-Draws.aspx?e=580&y=2011
   *
   */
  def parseTournament(tournamentUrl: String): List[APIMatch]

  /**
   * @param url e.g. http://www.atpworldtour.com/Share/Match-Facts-Pop-Up.aspx?t=580&y=2011&r=1&p=N409
   */
  def parseMatchFacts(tennisMatch: APIMatch): APIMatchFacts
}