package dk.atp.api

import TournamentAtpApi._
import java.util.Date

/**
 * Obtains all tennis tournaments from http://www.atpworldtour.com/ page for a given year.
 *
 * @author korzekwad
 */
object TournamentAtpApi {

  case class Tournament(matches:List[Match])
  case class Match(marketTime:Date,players:List[String])
}

trait TournamentAtpApi {

  /**
   * @param year
   * @param maxNumOfTournaments Maximum number of tournaments to be parsed
   */
  def parseTournaments(year: Int, maxNumOfTournaments: Int): List[Tournament]
}