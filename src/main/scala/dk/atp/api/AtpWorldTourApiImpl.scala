package dk.atp.api

import scala.collection.JavaConversions.asScalaBuffer

import org.jsoup.Jsoup

import AtpWorldTourApi.PointWonFactEnum.PointWonFactEnum
import AtpWorldTourApi.SurfaceEnum.SurfaceEnum
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

    val doc = Jsoup.connect("http://www.atpworldtour.com/Matchfacts/Matchfacts-List.aspx?c=1&s=1&y=2010").get();
    val factsDiv = doc.getElementsByClass("reliabilityTableModuleInner")
    val factsTable = factsDiv.first().getElementsByTag("tbody")
    val playerFactsSrc = factsTable.first().children()

    val playerFacts = for {
      playerFactSrc <- playerFactsSrc
      val pctWon = playerFactSrc.getElementsByClass("stat").first.text.toInt
      val matches = playerFactSrc.getElementsByClass("matches").first.text.toInt
      val playerFact = FirstServeFact(pctWon, matches)
    } yield playerFact

    FirstServeFacts(playerFacts.toList)
  }

  /**Match facts statistics http://www.atpworldtour.com/Matchfacts/Matchfacts-Landing.aspx*/
  def pointWonFacts(pointWonFact: PointWonFactEnum, surface: SurfaceEnum, year: Int): PointWonFacts = throw new UnsupportedOperationException("Not implemented yet.")

}