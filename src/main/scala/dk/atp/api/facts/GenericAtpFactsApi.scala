package dk.atp.api.facts

import scala.collection.JavaConversions.asScalaBuffer
import org.jsoup.Jsoup
import AtpFactsApi._
import PointWonFactEnum.PointWonFactEnum
import dk.atp.api.domain.SurfaceEnum._
import org.jsoup.select.Elements
import org.jsoup.nodes.Element
import org.jsoup.nodes._

/**
 * API interface for atpworldtour.com tennis statistics.
 *
 * @param timeout Set the request timeouts (connect and read).
 * If a timeout occurs, an IOException will be thrown. The default timeout is 5 seconds (5000 millis).
 * A timeout of zero is treated as an infinite timeout.
 */
class GenericAtpFactsApi(timeout: Int = 5000) extends AtpFactsApi {

  /**Match facts statistics http://www.atpworldtour.com/Matchfacts/Matchfacts-Landing.aspx*/
  def firstServeFacts(surface: SurfaceEnum, year: Int): FirstServeFacts = {

    val atpQuery = "http://www.atpworldtour.com/Matchfacts/Matchfacts-List.aspx?c=1&s=%s&y=%s".format(surface.id, year)
    val playerFactsSrc = getPlayersFactsSrc(atpQuery)

    val playerFacts: List[FirstServeFact] = playerFactsSrc.map(toFirstServeFact).toList

    FirstServeFacts(playerFacts)
  }

  /**Match facts statistics http://www.atpworldtour.com/Matchfacts/Matchfacts-Landing.aspx*/
  def pointWonFacts(pointWonFact: PointWonFactEnum, surface: SurfaceEnum, year: Int): PointWonFacts = {
    val atpQuery = "http://www.atpworldtour.com/Matchfacts/Matchfacts-List.aspx?c=%s&s=%s&y=%s".format(pointWonFact.id, surface.id, year)
    val playerFactsSrc = getPlayersFactsSrc(atpQuery)

    val playerFacts = playerFactsSrc.map(toPointWonFact).toList

    PointWonFacts(playerFacts)
  }

  private def getPlayersFactsSrc(atpQueryUrl: String): Elements = {
    val doc = Jsoup.connect(atpQueryUrl).timeout(timeout).get();
    val factsDiv = doc.getElementsByClass("reliabilityTableModuleInner")
    val factsTable = factsDiv.first().getElementsByTag("tbody")
    val playerFactsSrc = factsTable.first().children()
    playerFactsSrc
  }

  private def toFirstServeFact(playerFactSrc: Element): FirstServeFact = {
    val rank = playerFactSrc.getElementsByClass("rank").first.text.split("\\.")(0).toInt
    val fullName = playerFactSrc.getElementsByClass("player").first().child(0).html().replace("&nbsp;", " ")
    val stat = playerFactSrc.getElementsByClass("stat").first.text.toDouble
    val matches = playerFactSrc.getElementsByClass("matches").first.text.toInt
    val playerFact = FirstServeFact(rank, fullName, stat, matches)
    playerFact
  }

  private def toPointWonFact(playerFactSrc: Element): PointWonFact = {
    val rank = playerFactSrc.getElementsByClass("rank").first.text.split("\\.")(0).toInt
    val fullName = playerFactSrc.getElementsByClass("player").first().child(0).html().replace("&nbsp;", " ")
    val stat = playerFactSrc.getElementsByClass("stat").first.text.toDouble
    val number = playerFactSrc.getElementsByClass("number").first.text.toInt
    val total = playerFactSrc.getElementsByClass("total").first.text.toInt
    val matches = playerFactSrc.getElementsByClass("matches").first.text.toInt
    val playerFact = PointWonFact(rank, fullName, number, total, stat, matches)
    playerFact
  }

  /**Player facts statistics http://www.atpworldtour.com/Tennis/Players/Top-Players/Rafael-Nadal.aspx?t=mf.*/
  def playerFacts(fullName: String, surface: SurfaceEnum, year: Int): PlayerFacts = {
    val doc = getPlayerFactsSrc(fullName, surface, year)

    val factLeftDiv = doc.getElementsByClass("bioMatchfactsCol")
    val factRightDiv = doc.getElementsByClass("bioMatchfactsCol2")

    require(factLeftDiv.first() != null && factLeftDiv.first().getElementsMatchingOwnText("^1st Serve$") != null, "No data available or wrong data format.")

    val firstServePct = factLeftDiv.first().getElementsMatchingOwnText("^1st Serve$").text().split("%")(0).toDouble
    val firstServeWonPct = factLeftDiv.first().getElementsMatchingOwnText("^1st Serve Points Won$").first().getElementsByTag("span").text.split("%")(0).toDouble
    val secondServeWonPct = factLeftDiv.first().getElementsMatchingOwnText("^2nd Serve Points Won$").first().getElementsByTag("span").text.split("%")(0).toDouble
    val serviceGamesPlayed = factLeftDiv.first().getElementsMatchingOwnText("^Service Games Played$").first().getElementsByTag("span").text.toInt

    val firstReturnWonPct = factRightDiv.first().getElementsMatchingOwnText("^1st Serve Return Points Won$").first().getElementsByTag("span").text.split("%")(0).toDouble
    val secondReturnWonPct = factRightDiv.first().getElementsMatchingOwnText("^2nd Serve Return Points Won$").first().getElementsByTag("span").text.split("%")(0).toDouble
    val returnGamesPlayed = factRightDiv.first().getElementsMatchingOwnText("^Return Games Played$").first().getElementsByTag("span").text.toInt

    PlayerFacts(firstServePct, firstServeWonPct, secondServeWonPct, firstReturnWonPct, secondReturnWonPct, serviceGamesPlayed, returnGamesPlayed)
  }

  private def getPlayerFactsSrc(fullName: String, surface: SurfaceEnum, year: Int): Document = {
    val fullNameEncoded = fullName.replace(" ", "-")
    val atpQuery = "http://www.atpworldtour.com/Tennis/Players/Top-Players/%s.aspx?t=mf&y=%s&s=%s#".format(fullNameEncoded, year, surface.id)
    val response = Jsoup.connect(atpQuery).timeout(timeout).ignoreHttpErrors(true).execute();
    val doc = if (response.statusCode() == 200) response.parse() else {
      val fullNameArray = fullName.split(" ")
      val surname = fullNameArray.last
      val firstName = fullNameArray.head
      val altAtpQuery = atpQuery.replace("Top-Players", surname.subSequence(0,2) + "/" + firstName)
      Jsoup.connect(altAtpQuery).timeout(timeout).get();
    }

    doc
  }
}