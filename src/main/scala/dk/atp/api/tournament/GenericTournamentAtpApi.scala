package dk.atp.api.tournament

import TournamentAtpApi._
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.jsoup.nodes.Element
import org.jsoup.nodes._
import scala.collection.JavaConversions._
import java.util.Date
import dk.atp.api.AtpWorldTourApi.SurfaceEnum._
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
    val tournaments = (1 to 2).flatMap(category => parseTournamentsForCategory(category,year))
    tournaments.toList
  }

  def parseTournamentsForCategory(tournamentCategory: Int, year: Int): List[Tournament] = {
    val tournamentsUrl = "http://www.atpworldtour.com/Scores/Archive-Event-Calendar.aspx?t=%s&y=%s".format(tournamentCategory,year)

    val tournamentsDoc = parseUrl(tournamentsUrl)
    val tournamentsData = tournamentsDoc.getElementsByClass("calendarFilterItem")

    val numOfSet = tournamentCategory match {
      case 1 => 3 //Grand slam is a best of 5 sets tournament
      case 2 => 2
    }

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
      val href = e.child(4).child(0).attr("href")
      val tournamentRef = if (href.isEmpty()) "" else "http://www.atpworldtour.com" + href
    } yield Tournament(tournamentTime, tournamentName, surface, numOfSet, tournamentRef)

    tournaments.toList
  }

  /**
   * @param tournamentUrl, e.g. http://www.atpworldtour.com/Share/Event-Draws.aspx?e=580&y=2011
   *
   */
  def parseTournament(tournamentUrl: String): List[Match] = {
    val doc = parseUrl(tournamentUrl)

    val scoreDocs = doc.getElementsByClass("draws_page").get(0).getElementsByClass("score").filter(score => score.getElementsByTag("a").size() == 1)
    val matches = for {
      scpreDoc <- scoreDocs
      val scoreValue = scpreDoc.child(0).text
      val matchStatsUrl = "http://www.atpworldtour.com" + (scpreDoc.child(0).attr("onclick").split("'")(1))
    } yield Match(scoreValue, matchStatsUrl)
    matches.toList
  }

  /**
   * @param matchFactsUrl e.g. http://www.atpworldtour.com/Share/Match-Facts-Pop-Up.aspx?t=580&y=2011&r=1&p=N409
   */
  def parseMatchFacts(matchFactsUrl: String): MatchFacts = {

    val matchStatsDoc = parseUrl(matchFactsUrl)

    val round = matchStatsDoc.getElementsByClass("infoRow").get(1).text
    val durationMinutes = matchStatsDoc.getElementsByClass("infoRow").get(2).child(0).text.split("\\D")(0).toInt

    val totalServicePointsWonText = matchStatsDoc.getElementsByClass("infoRow").get(17)
    val List(playerATotalServicePoints, playerATotalServicePointsWon) = totalServicePointsWonText.child(0).text.split("\\D").takeRight(2).map(_.toInt).toList
    val List(playerBTotalServicePoints, playerBTotalServicePointsWon) = totalServicePointsWonText.child(1).text.split("\\D").takeRight(2).map(_.toInt).toList

    val List(winner, playerA, playerB) = matchStatsDoc.getElementsByClass("playerName").map(e => e.text).toList

    MatchFacts(PlayerFacts(playerA, playerATotalServicePoints, playerATotalServicePointsWon),
      PlayerFacts(playerB, playerBTotalServicePoints, playerBTotalServicePointsWon), winner, round, durationMinutes)
  }

  private def parseUrl(url: String): Element = Jsoup.connect(url).timeout(timeout).get()

}