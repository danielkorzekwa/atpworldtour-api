package dk.atp.api

import TournamentAtpApi._
import java.util.Date
import AtpWorldTourApi.SurfaceEnum._

/**
 * Obtains all tennis tournaments from http://www.atpworldtour.com/ page for a given year.
 *
 * @author korzekwad
 */
object TournamentAtpApi {

  /**
   * @param numOfSet 2 (THREE_SET_MATCH) or 3 (FIVE_SET_MATCH)
   */
  case class Tournament(tournamentTime:Date,tournamentName:String, surface: SurfaceEnum, numOfSet:Int, tournamentUrl:String)
  case class Match(score: String, matchFactsUrl:String)
  
  case class MatchFacts(playerAFacts:PlayerFacts,playerBFacts:PlayerFacts, winner:String, round:String, durationMinutes: Int)
  case class PlayerFacts(playerName:String,totalServicePoints:Int,totalServicePointsWon:Int)
}

trait TournamentAtpApi {

  /**
   * @param year
   */
  def parseTournaments(year: Int): List[Tournament]
  
  /**
   * @param tournamentUrl, e.g. http://www.atpworldtour.com/Share/Event-Draws.aspx?e=580&y=2011
   * 
   */
  def parseTournament(tournamentUrl: String) :List[Match]
  
  /**
   * @param url e.g. http://www.atpworldtour.com/Share/Match-Facts-Pop-Up.aspx?t=580&y=2011&r=1&p=N409
   */
  def parseMatchFacts(matchFactsUrl: String): MatchFacts
}