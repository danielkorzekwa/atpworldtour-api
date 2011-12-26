package dk.atp.api

/**
 * API interface for atpworldtour.com tennis statistics.
 *
 */
trait AtpWorldTourApi {

  object MatchfactEnum extends Enumeration {
    type MatchFactEnum = Value
    val FIRST_SERVE = Value("1")
    val FIRST_SERVE_POINTS_WON = Value("2")
    val SECOND_SERVE_POINTS_WON = Value("3")
    val FIRST_SERVE_RETURN_POINTS_WON = Value("6")
    val SECOND_SERVE_RETURN_POINTS_WON = Value("7")

    override def toString() = MatchfactEnum.values.mkString("MatchfactEnum [", ", ", "]")
  }
}