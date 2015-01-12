package dk.atp.api.tournament

import TournamentAtpApi._
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.jsoup.nodes.Element
import org.jsoup.nodes._
import scala.collection.JavaConversions._
import java.util.Date
import java.text.SimpleDateFormat
import dk.atp.api.domain.SurfaceEnum._

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
  def parseTournaments(year: Int): List[APITournament] = {

    /** There are two categories: 1 - Grand slam, 2 - ATP Wourld Tour. Other categories for ATP challengers and ITF futures are ignored.*/
    val tournaments = (1 to 2).flatMap(category => parseTournamentsForCategory(category, year))
    tournaments.toList
  }

  def parseTournamentsForCategory(tournamentCategory: Int, year: Int): List[APITournament] = {
    val tournamentsUrl = "http://www.atpworldtour.com/Scores/Archive-Event-Calendar.aspx?t=%s&y=%s".format(tournamentCategory, year)

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
        case "carpet" => HARD
      }
      val href = e.child(4).child(0).attr("href")
      val tournamentRef = if (href.isEmpty()) "" else "http://www.atpworldtour.com" + href
    } yield APITournament(tournamentTime, tournamentName, surface, numOfSet, tournamentRef)

    tournaments.toList
  }

  /**
   * @param tournamentUrl, e.g. http://www.atpworldtour.com/Share/Event-Draws.aspx?e=580&y=2011
   *
   */
  def parseTournament(tournamentUrl: String): List[APIMatch] = {
    val doc = parseUrl(tournamentUrl)
    val scoreDocs = doc.getElementsByClass("scores").filter(score => score.getElementsByTag("a").size() == 1)
    val matches = for {
      scoreDoc <- scoreDocs
      val scoreValue = scoreDoc.child(0).text
      
      val matchStatsUrl = "http://www.atpworldtour.com" + (scoreDoc.child(0).attr("href").split("'")(1))
    } yield APIMatch2014(scoreValue, matchStatsUrl)
    matches.toList
  }

  
  
   def parseMatchFacts(tennisMatch:APIMatch): APIMatchFacts = {
      val matchFacts = tennisMatch match {
      case tennisMatch:APIMatch2014 =>parseMatchFacts(tennisMatch.matchFactsUrl)
      
    }
      matchFacts
   }
  
   
  /**
   * @param matchFactsUrl e.g. http://www.atpworldtour.com/Share/Match-Facts-Pop-Up.aspx?t=580&y=2011&r=1&p=N409
   */
  def parseMatchFacts(matchFactsUrl: String): APIMatchFacts = {

    val matchStatsDoc = parseUrl(matchFactsUrl)

    val round = matchStatsDoc.getElementsByClass("infoRow").get(1).text
    val durationMinutes = matchStatsDoc.getElementsByClass("infoRow").get(2).child(0).text.split("\\D")(0).toInt

    val acesText = matchStatsDoc.getElementsByClass("infoRow").get(6)
    val playerAAces = acesText.child(0).text.split("\\D")(0).toInt
    val playerBAces = acesText.child(1).text.split("\\D")(0).toInt

    val doubleFaultsText = matchStatsDoc.getElementsByClass("infoRow").get(7)
    val playerADoubleFaults = doubleFaultsText.child(0).text.split("\\D")(0).toInt
    val playerBDoubleFaults = doubleFaultsText.child(1).text.split("\\D")(0).toInt

    val firstServeText = matchStatsDoc.getElementsByClass("infoRow").get(8)
    val List(playerAFirstServeHits, playerAFirstServeTotal) = firstServeText.child(0).text.split("\\D").takeRight(2).map(_.toInt).toList
    val List(playerBFirstServeHits, playerBFirstServeTotal) = firstServeText.child(1).text.split("\\D").takeRight(2).map(_.toInt).toList

    val firstServePointsWonText = matchStatsDoc.getElementsByClass("infoRow").get(9)
    val List(playerAFirstServeWon, playerAFirstServePlayed) = firstServePointsWonText.child(0).text.split("\\D").takeRight(2).map(_.toInt).toList
    val List(playerBFirstServeWon, playerBFirstServePlayed) = firstServePointsWonText.child(1).text.split("\\D").takeRight(2).map(_.toInt).toList

    val secondServePointsWonText = matchStatsDoc.getElementsByClass("infoRow").get(10)
    val List(playerASecondServeWon, playerASecondServePlayed) = secondServePointsWonText.child(0).text.split("\\D").takeRight(2).map(_.toInt).toList
    val List(playerBSecondServeWon, playerBSecondServePlayed) = secondServePointsWonText.child(1).text.split("\\D").takeRight(2).map(_.toInt).toList

    val breakPointsSavedText = matchStatsDoc.getElementsByClass("infoRow").get(11)
    val List(playerABreakPointsSaved, playerABreakPointsTotal) = breakPointsSavedText.child(0).text.split("\\D").takeRight(2).map(_.toInt).toList
    val List(playerBBreakPointsSaved, playerBBreakPointsTotal) = breakPointsSavedText.child(1).text.split("\\D").takeRight(2).map(_.toInt).toList

    val serviceGamesPlayedText = matchStatsDoc.getElementsByClass("infoRow").get(12)
    val playerAServiceGames = serviceGamesPlayedText.child(0).text.split("\\D")(0).toInt
    val playerBServiceGames = serviceGamesPlayedText.child(1).text.split("\\D")(0).toInt

    val totalServicePointsWonText = matchStatsDoc.getElementsByClass("infoRow").get(17)
    val List(playerATotalServicePointsWon, playerATotalServicePoints) = totalServicePointsWonText.child(0).text.split("\\D").takeRight(2).map(_.toInt).toList
    val List(playerBTotalServicePointsWon, playerBTotalServicePoints) = totalServicePointsWonText.child(1).text.split("\\D").takeRight(2).map(_.toInt).toList

    val List(winner, playerA, playerB) = matchStatsDoc.getElementsByClass("playerName").map(e => e.text).toList

    APIMatchFacts(APIPlayerFacts(playerA,
      playerAAces,
      playerADoubleFaults,
      playerAFirstServeHits, playerAFirstServeTotal,
      playerAFirstServeWon, playerAFirstServePlayed,
      playerASecondServeWon, playerASecondServePlayed,
      playerABreakPointsSaved, playerABreakPointsTotal,
      playerAServiceGames,
      playerATotalServicePointsWon, playerATotalServicePoints),
      APIPlayerFacts(playerB,
        playerBAces,
        playerBDoubleFaults,
        playerBFirstServeHits, playerBFirstServeTotal,
        playerBFirstServeWon, playerBFirstServePlayed,
        playerBSecondServeWon, playerBSecondServePlayed,
        playerBBreakPointsSaved, playerBBreakPointsTotal,
        playerBServiceGames,
        playerBTotalServicePointsWon, playerBTotalServicePoints), winner, round, durationMinutes)
  }

  private def parseUrl(url: String): Element = Jsoup.connect(url).timeout(timeout).get()

}