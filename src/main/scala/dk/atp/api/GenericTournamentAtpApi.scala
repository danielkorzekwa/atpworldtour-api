package dk.atp.api

import TournamentAtpApi._
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.jsoup.nodes.Element
import org.jsoup.nodes._
import scala.collection.JavaConversions._
import java.util.Date
import AtpWorldTourApi.SurfaceEnum._
import java.text.SimpleDateFormat

/**
 * Obtains all tennis tournaments from http://www.atpworldtour.com/ page for a given year.
 *
 * @author korzekwad
 */
class GenericTournamentAtpApi(timeout: Int = 5000) extends TournamentAtpApi {

  private val DATE_FORMAT = "dd.MM.yyyy"

  /**
   * @param year
   */
  def parseTournaments(year: Int): List[Tournament] = {

    /** There are two categories: 1 - Grand slam, 2 - ATP Wourld Tour. Other categories for ATP challengers and ITF futures are ignored.*/
    val tournaments = (1 to 2).flatMap(category => parseTournamentsForCategory(category))
    tournaments.toList
  }

  def parseTournamentsForCategory(tournamentCategory: Int): List[Tournament] = {
    val tournamentsUrl = "http://www.atpworldtour.com/Scores/Archive-Event-Calendar.aspx?t=%s&y=2011"

    val tournamentsDoc = Jsoup.connect(tournamentsUrl.format(tournamentCategory)).get()
    val tournamentsData = tournamentsDoc.getElementsByClass("calendarFilterItem")

    val numOfSet = tournamentCategory match {
      case 1 => 3 //Grand slam is a best of 5 sets tournament
      case 2 => 2
    }

    collection.parallel.ForkJoinTasks.defaultForkJoinPool.setParallelism(1)
    val tournaments = for {
      e <- tournamentsData.iterator()

      val df = new SimpleDateFormat(DATE_FORMAT)
      val tournamentTime = df.parse(e.child(0).text())
      val tournamentName = e.child(1).text()
      val surfaceText = e.child(2).text().split(" ")(1).toLowerCase()
      val surface = surfaceText match {
        case "clay" => CLAY
        case "grass" => GRASS
        case "hard" => HARD
      }
      val tournamentRef = e.child(4).child(0).attr("href")
      val matches = if(!tournamentRef.isEmpty()) parseTournamentMatches(tournamentRef) else Nil
    } yield Tournament(tournamentTime, tournamentName, surface, numOfSet, matches)

    tournaments.toList
  }

  private def parseTournamentMatches(url: String): List[Match] = {
    
    val doc = Jsoup.connect("http://www.atpworldtour.com" + url).get()
   
    val roundsDoc = doc.getElementsByClass("draws_page").get(0).getElementsByTag("ol")
    val matches = roundsDoc.flatMap(r => toMatches(r))
    matches.toList
  }

  private def toMatches(round: Element): List[Match] = {
    val matches = for {
      matchDoc <- round.children

    } yield Match(new Date(0), Nil)

    matches.toList
  }
}