package dk.atp.api

import AtpWorldTourApi._
import SurfaceEnum._
import PointWonFactEnum._

/**
 * API interface for atpworldtour.com tennis statistics.
 *
 */
class AtpWorldTourApiImpl extends AtpWorldTourApi {

  /**Match facts statistics http://www.atpworldtour.com/Matchfacts/Matchfacts-Landing.aspx*/
  def firstServeFacts(surface: SurfaceEnum, year: Int): FirstServeFacts = throw new UnsupportedOperationException("Not implemented yet.")

  /**Match facts statistics http://www.atpworldtour.com/Matchfacts/Matchfacts-Landing.aspx*/
  def pointWonFacts(pointWonFact: PointWonFactEnum, surface: SurfaceEnum, year: Int): PointWonStats = throw new UnsupportedOperationException("Not implemented yet.")

}