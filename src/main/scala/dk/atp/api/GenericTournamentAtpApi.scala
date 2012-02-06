package dk.atp.api

import TournamentAtpApi._
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.jsoup.nodes.Element
import org.jsoup.nodes._
import scala.collection.JavaConversions._
import java.util.Date
import AtpWorldTourApi.SurfaceEnum._

/**
 * Obtains all tennis tournaments from http://www.atpworldtour.com/ page for a given year.
 *
 * @author korzekwad
 */
class GenericTournamentAtpApi(timeout: Int = 5000) extends TournamentAtpApi {

  /**
   * @param year
   * @param maxNumOfTournaments Maximum number of tournaments to be parsed
   */
  def parseTournaments(year: Int, maxNumOfTournaments: Int): List[Tournament] = {
    val tournamentsUrl = "http://www.atpworldtour.com/Scores/Archive-Event-Calendar.aspx?t=%s&y=2011"

    /** There are two categories: 1 - Grand slam, 2 - ATP Wourld Tour. Other categories for ATP challengers and ITF futures are ignored.*/
    val tournaments = (1 to 2).flatMap(category => parseTournaments(tournamentsUrl.format(category)))
    tournaments.toList
  }

  private def parseTournaments(url: String): List[Tournament] = {

    val tournamentsDoc = Jsoup.connect(url).get()

    val tournamentsData = tournamentsDoc.getElementsByClass("calendarFilterItem")
    val tournaments = tournamentsData.iterator().map(e => toTournament(e))
    tournaments.toList
  }

  private def toTournament(e: Element): Tournament = {
    Tournament(new Date(0),"",HARD,1,Nil)
  }

}