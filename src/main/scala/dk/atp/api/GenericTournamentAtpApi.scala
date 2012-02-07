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

    val tournamentsDoc = Jsoup.connect(tournamentsUrl.format(tournamentCategory)).timeout(timeout).get()
    val tournamentsData = tournamentsDoc.getElementsByClass("calendarFilterItem")

    val numOfSet = tournamentCategory match {
      case 1 => 3 //Grand slam is a best of 5 sets tournament
      case 2 => 2
    }

    collection.parallel.ForkJoinTasks.defaultForkJoinPool.setParallelism(32)
    val tournaments = for {
      e <- tournamentsData.iterator().toList.par

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
      val matches = if (!tournamentRef.isEmpty()) parseTournamentMatches(tournamentRef, tournamentTime) else Nil
    } yield Tournament(tournamentTime, tournamentName, surface, numOfSet, matches)

    tournaments.toList
  }

  private def parseTournamentMatches(url: String, tournamentTime: Date): List[Match] = {

    val doc = Jsoup.connect("http://www.atpworldtour.com" + url).timeout(timeout).get()

    val scoreDoc = doc.getElementsByClass("draws_page").get(0).getElementsByClass("score").filter(score => score.getElementsByTag("a").size() == 1)
    val matches = scoreDoc.map(score => toMatch(score, tournamentTime))
    matches.toList
  }

  private def toMatch(score: Element, tournamentTime: Date): Match = {

    val scoreValue = score.child(0).text
    val matchStatsUrl = "http://www.atpworldtour.com" + (score.child(0).attr("onclick").split("'")(1))
    
    val matchStatsDoc = Jsoup.connect(matchStatsUrl).timeout(timeout).get()
    val List(winner,playerA,playerB) = matchStatsDoc.getElementsByClass("playerName").map(e => e.text).toList
   
    Match(tournamentTime, List(playerA,playerB),winner,scoreValue)

  }
}