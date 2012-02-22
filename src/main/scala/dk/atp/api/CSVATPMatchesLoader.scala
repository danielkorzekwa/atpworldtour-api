package dk.atp.api

import ATPMatchesLoader._
import org.joda.time._
import org.apache.commons.io.FileUtils._
import java.io.File
import scala.collection.JavaConversions._

object CSVATPMatchesLoader {

  /**Writes markets data to csv file.*/
  def toCSVFile(markets: List[MatchComposite], marketsDataFile: String) {
    val fileOut = new File(marketsDataFile)
    val marketData: List[String] = markets.map(m => "market record")
    writeLines(fileOut, marketData)
  }
}

class CSVATPMatchesLoader(markets: List[MatchComposite]) extends ATPMatchesLoader {
  def loadMarkets(year: Int): List[MatchComposite] = markets.filter(m => new DateTime(m.tournament.tournamentTime).getYear() == year)
}