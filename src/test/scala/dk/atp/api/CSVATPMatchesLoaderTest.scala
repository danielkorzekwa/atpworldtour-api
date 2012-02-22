package dk.atp.api

import org.junit._
import Assert._
import ATPMatchesLoader._
import dk.atp.api.tournament.TournamentAtpApi._
import org.joda.time._
import dk.atp.api.AtpWorldTourApi.SurfaceEnum._
import scala.io.Source

class CSVATPMatchesLoaderTest {

  /**Tests for loadMarkets*/

  @Test def loadMarketsSingleMarket {
    val marketsSource = matchComposite(DateTime.parse("2011-01-17")) :: matchComposite(DateTime.parse("2012-01-17")) :: Nil
    val matchesLoader = new CSVATPMatchesLoader(marketsSource)

    val markets = matchesLoader.loadMarkets(2011)
    assertEquals(1, markets.size)
    assertEquals(marketsSource(0).tournament.tournamentTime, markets(0).tournament.tournamentTime)
  }

  @Test def loadMarketsTwoMarketsMarket {
    val marketsSource = matchComposite(DateTime.parse("2012-01-17")) :: matchComposite(DateTime.parse("2011-01-17")) :: matchComposite(DateTime.parse("2012-01-17")) :: Nil
    val matchesLoader = new CSVATPMatchesLoader(marketsSource)

    val markets = matchesLoader.loadMarkets(2012)
    assertEquals(2, markets.size)
    assertEquals(marketsSource(0).tournament.tournamentTime, markets(0).tournament.tournamentTime)
    assertEquals(marketsSource(2).tournament.tournamentTime, markets(1).tournament.tournamentTime)
  }

  @Test def loadMarketsNoDataForAYear {
    val marketsSource = matchComposite(DateTime.parse("2011-01-17")) :: Nil
    val matchesLoader = new CSVATPMatchesLoader(marketsSource)

    val markets = matchesLoader.loadMarkets(2010)
    assertEquals(0, markets.size)
  }

  /**Tests for CSVATPMatchesLoader.toCSVFile*/
  @Test def toCSVFile {
    val marketsSource = matchComposite(DateTime.parse("2012-01-17")) :: matchComposite(DateTime.parse("2011-01-17")) :: matchComposite(DateTime.parse("2012-01-17")) :: Nil

    val marketDataFile = "./target/market_data_file.csv"
    CSVATPMatchesLoader.toCSVFile(marketsSource, marketDataFile)

    val marketData = Source.fromFile(marketDataFile)
    assertEquals(3, marketData.reset().getLines().size)
  }

  private def matchComposite(eventTime: DateTime): MatchComposite =
    MatchComposite(Tournament(eventTime.toDate(), "Australian Open Australia Grand Slams", HARD, 3, "http://www.atpworldtour.com/Share/Event-Draws.aspx?e=580&y=2011"), null, null)
}