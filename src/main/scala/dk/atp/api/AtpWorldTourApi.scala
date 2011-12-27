package dk.atp.api

import AtpWorldTourApi._

/**
 * API interface for atpworldtour.com tennis statistics.
 *
 */

object AtpWorldTourApi {

  object PointWonFactEnum extends Enumeration {
    type PointWonFactEnum = Value
    val FIRST_SERVE_POINTS_WON = Value("2")
    val SECOND_SERVE_POINTS_WON = Value("3")
    val FIRST_SERVE_RETURN_POINTS_WON = Value("6")
    val SECOND_SERVE_RETURN_POINTS_WON = Value("7")

    override def toString() = PointWonFactEnum.values.mkString("MatchfactEnum [", ", ", "]")
  }
  
  object SurfaceEnum extends Enumeration {
    type SurfaceEnum = Value
    val CLAY = Value(1)
    val GRASS = Value(2)
    val HARD = Value(3)

    override def toString() = SurfaceEnum.values.mkString("SurfaceEnum [", ", ", "]")
  }

  /**
   * @param rank
   * @param fullName
   * @param pctWon Percentage of first serve won
   * @param matches Total number of matches
   *
   */
  case class FirstServeFact(rank:Int,fullName: String, pctWon: Double, matches: Int)
  case class FirstServeFacts(playerFacts: List[FirstServeFact])

  /**
   * @param fullName
   * @param pointsWon Number of points won
   * @param totalPoints Total number of points
   * @param pctWon Percentage of matches won
   * @param matches Total number of matches
   */
  case class PointWonFact(fullName: String, pointsWon: Int, totalPoints: Int, pctWon: Double, matches: Int)
  case class PointWonFacts(playerFacts: List[PointWonFact])

}

import AtpWorldTourApi.PointWonFactEnum._
import AtpWorldTourApi.SurfaceEnum._

trait AtpWorldTourApi {

  /**Match facts statistics http://www.atpworldtour.com/Matchfacts/Matchfacts-Landing.aspx*/
  def firstServeFacts(surface: SurfaceEnum, year: Int): FirstServeFacts

  /**Match facts statistics http://www.atpworldtour.com/Matchfacts/Matchfacts-Landing.aspx*/
  def pointWonFacts(pointWonFact: PointWonFactEnum, surface: SurfaceEnum, year: Int): PointWonFacts

}
