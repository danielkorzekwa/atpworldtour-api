package dk.atp.api

import dk.atp.api.tournament.TournamentAtpApi._
import domain._
import dk.atp.api.domain.SurfaceEnum
import dk.atp.api.domain.SurfaceEnum._
import tournament.TournamentAtpApi._
import java.text.SimpleDateFormat

/**Load matches from http://www.atpworldtour.com/ web site.*/

trait ATPMatchesLoader {

  def loadMatches(year: Int): List[TennisMatch]

}