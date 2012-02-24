package dk.atp.api.facts

import AtpFactsApi._
import dk.atp.api.domain.SurfaceEnum._

/**
 * API interface for atpworldtour.com tennis statistics.
 *
 */

object AtpFactsApi {

  object PointWonFactEnum extends Enumeration {
    type PointWonFactEnum = Value
    val FIRST_SERVE_POINTS_WON = Value(2)
    val SECOND_SERVE_POINTS_WON = Value(3)
    val FIRST_SERVE_RETURN_POINTS_WON = Value(6)
    val SECOND_SERVE_RETURN_POINTS_WON = Value(7)

    override def toString() = PointWonFactEnum.values.mkString("MatchfactEnum [", ", ", "]")
  }
  
  /**
   * @param rank
   * @param fullName
   * @param firstServePct Percentage of first serve correct
   * @param matches Total number of matches
   *
   */
  case class FirstServeFact(rank:Int,fullName: String, firstServePct: Double, matches: Int)
  case class FirstServeFacts(playerFacts: List[FirstServeFact]) {
    
    /**Calculates average first point percentage over all players.*/
    def firstServeAvgPct():Double = playerFacts.foldLeft(0d)((sum,fact) => sum + fact.firstServePct) / playerFacts.size
  }

  /**
   * @param rank
   * @param fullName
   * @param pointsWon Number of points won
   * @param totalPoints Total number of points
   * @param pctWon Percentage of matches won
   * @param matches Total number of matches
   */
  case class PointWonFact(rank:Int,fullName: String, pointsWon: Int, totalPoints: Int, pctWon: Double, matches: Int)
  case class PointWonFacts(playerFacts: List[PointWonFact]) {
     /**Calculates average point won percentage over all players.*/
    def pointWonAvgPct():Double = playerFacts.foldLeft(0d)((sum,fact) => sum + fact.pctWon) / playerFacts.size
  }
  
  /**
   * @param firstServePct Percentage of first correct serves.
   * @param firstServeWonPct Percentage of points won on first serve.
   * @param secondServeWonPct Percentage of points won on second serve.
   * @param firstReturnWonPct Percentage of points won on first serve return.
   * @param secondReturnWonPct Percentage of points won on second serve return.
   * @param serviceGamesPlayed Number of service games played.
   * @param returnGamesPlayed Number of return games played.
   */
  case class PlayerFacts(firstServePct:Double,firstServeWonPct:Double,secondServeWonPct:Double,firstReturnWonPct:Double,secondReturnWonPct:Double, serviceGamesPlayed:Int,returnGamesPlayed:Int)

}

import AtpFactsApi.PointWonFactEnum._


trait AtpFactsApi {

  /**Match facts statistics http://www.atpworldtour.com/Matchfacts/Matchfacts-Landing.aspx*/
  def firstServeFacts(surface: SurfaceEnum, year: Int): FirstServeFacts

  /**Match facts statistics http://www.atpworldtour.com/Matchfacts/Matchfacts-Landing.aspx*/
  def pointWonFacts(pointWonFact: PointWonFactEnum, surface: SurfaceEnum, year: Int): PointWonFacts
  
  /**Player facts statistics http://www.atpworldtour.com/Tennis/Players/Top-Players/Rafael-Nadal.aspx?t=mf.*/
  def playerFacts(fullName:String,surface: SurfaceEnum, year: Int):PlayerFacts

}
