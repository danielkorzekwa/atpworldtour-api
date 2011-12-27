package dk.atp.api

import scala.collection.JavaConversions.asScalaBuffer

import org.jsoup.Jsoup

import AtpWorldTourApi.PointWonFactEnum.PointWonFactEnum
import AtpWorldTourApi.SurfaceEnum.SurfaceEnum
import AtpWorldTourApi.SurfaceEnum._
import AtpWorldTourApi.FirstServeFact
import AtpWorldTourApi.FirstServeFacts
import AtpWorldTourApi.PointWonFacts

/**
 * API interface for atpworldtour.com tennis statistics.
 *
 */
class AtpWorldTourApiImpl extends AtpWorldTourApi {

  /**Match facts statistics http://www.atpworldtour.com/Matchfacts/Matchfacts-Landing.aspx*/
  def firstServeFacts(surface: SurfaceEnum, year: Int): FirstServeFacts = {

    val atpQuery = "http://www.atpworldtour.com/Matchfacts/Matchfacts-List.aspx?c=1&s=%s&y=%s".format(surface.id, year)

    val doc = Jsoup.connect(atpQuery).get();
    val factsDiv = doc.getElementsByClass("reliabilityTableModuleInner")
    val factsTable = factsDiv.first().getElementsByTag("tbody")
    val playerFactsSrc = factsTable.first().children()

    val playerFacts = for {
      playerFactSrc <- playerFactsSrc
      val rank = playerFactSrc.getElementsByClass("rank").first.text.split("\\.")(0).toInt
      val fullName = playerFactSrc.getElementsByClass("player").first().child(0).html().replace("&nbsp;", " ")
      val pctWon = playerFactSrc.getElementsByClass("stat").first.text.toInt
      val matches = playerFactSrc.getElementsByClass("matches").first.text.toInt
      val playerFact = FirstServeFact(rank, fullName, pctWon, matches)
    } yield playerFact

    FirstServeFacts(playerFacts.toList)
  }

  /**Match facts statistics http://www.atpworldtour.com/Matchfacts/Matchfacts-Landing.aspx*/
  def pointWonFacts(pointWonFact: PointWonFactEnum, surface: SurfaceEnum, year: Int): PointWonFacts = throw new UnsupportedOperationException("Not implemented yet.")

}