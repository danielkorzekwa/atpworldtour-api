package dk.atp.api

import TournamentAtpApi._

/**
 * Obtains all tennis tournaments from http://www.atpworldtour.com/ page for a given year.
 *
 * @author korzekwad
 */
object GenericTournamentAtpApi extends TournamentAtpApi {

  /**
   * @param year
   * @param maxNumOfTournaments Maximum number of tournaments to be parsed
   */
  def parseTournaments(year: Int, maxNumOfTournaments: Int): List[Tournament] = Nil
}