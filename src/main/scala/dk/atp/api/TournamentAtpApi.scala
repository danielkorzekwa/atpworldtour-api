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

  case class Tournament(tournamentTime:Date,tournamentName:String, surface: SurfaceEnum, numOfSet:Int, matches:List[Match])
  case class Match(marketTime:Date,players:List[String], winner: String, score: String)
}

trait TournamentAtpApi {

  /**
   * @param year
   */
  def parseTournaments(year: Int): List[Tournament]
}