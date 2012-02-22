package dk.atp.api

import org.junit._
import Assert._
import ATPMatchesLoader._
import dk.atp.api.tournament.TournamentAtpApi._
import org.joda.time._
import dk.atp.api.AtpWorldTourApi.SurfaceEnum._
import scala.io.Source
import tournament.GenericTournamentAtpApi

class CSVATPMatchesLoaderTest {

  /**Tests for loadMatches*/

  @Test def loadSingleMatch {
    val matchesSource = matchComposite(DateTime.parse("2011-01-17")) :: matchComposite(DateTime.parse("2012-01-17")) :: Nil
    val matchesLoader = new CSVATPMatchesLoader(matchesSource)

    val matches = matchesLoader.loadMatches(2011)
    assertEquals(1, matches.size)
    assertEquals(matchesSource(0).tournament.tournamentTime, matches(0).tournament.tournamentTime)
  }

  @Test def loadTwoMatches {
    val matchesSource = matchComposite(DateTime.parse("2012-01-17")) :: matchComposite(DateTime.parse("2011-01-17")) :: matchComposite(DateTime.parse("2012-01-17")) :: Nil
    val matchesLoader = new CSVATPMatchesLoader(matchesSource)

    val matches = matchesLoader.loadMatches(2012)
    assertEquals(2, matches.size)
    assertEquals(matchesSource(0).tournament.tournamentTime, matches(0).tournament.tournamentTime)
    assertEquals(matchesSource(2).tournament.tournamentTime, matches(1).tournament.tournamentTime)
  }

  @Test def loadMatchesNoDataForAYear {
    val matchesSource = matchComposite(DateTime.parse("2011-01-17"), "Rafael Nadal", "Marcos Daniel") :: Nil
    val matchesLoader = new CSVATPMatchesLoader(matchesSource)

    val matches = matchesLoader.loadMatches(2010)
    assertEquals(0, matches.size)
  }

  /**Tests for CSVATPMatchesLoader.toCSVFile*/
  @Test def toCSVFile {
    val matchesSource = matchComposite(DateTime.parse("2012-02-12T10:20:30"), "Rafael Nadal2", "Marcos Daniel2") ::
      matchComposite(DateTime.parse("2011-01-17"), "Rafael Nadal", "Marcos Daniel") ::
      matchComposite(DateTime.parse("2012-05-19"), "Rafael Nadal3", "Marcos Daniel3") :: Nil

    val matchDataFile = "./target/matches_data_file.csv"
    CSVATPMatchesLoader.toCSVFile(matchesSource, matchDataFile)

    val matchData = Source.fromFile(matchDataFile)
    assertEquals(4, matchData.reset().getLines().size)

    assertEquals("event_time,event_name,surface,num_of_sets,playerA,playerB,winner,score,round,duration_minutes,playerATotalServicePointsWon,playerATotalServicePoints,playerBTotalServicePointsWon,playerBTotalServicePoints", matchData.reset().getLine(1))
    assertEquals("2011-01-17 00:00:00.000,Australian Open Australia Grand Slams,HARD,3,Rafael Nadal,Marcos Daniel,Rafael Nadal,6-0; 5-0 RET,56,25,35,2,26", matchData.reset().getLine(2))
    assertEquals("2012-02-12 10:20:30.000,Australian Open Australia Grand Slams,HARD,3,Rafael Nadal2,Marcos Daniel2,Rafael Nadal,6-0; 5-0 RET,56,25,35,2,26", matchData.reset().getLine(3))
    assertEquals("2012-05-19 00:00:00.000,Australian Open Australia Grand Slams,HARD,3,Rafael Nadal3,Marcos Daniel3,Rafael Nadal,6-0; 5-0 RET,56,25,35,2,26", matchData.reset().getLine(4))
  }

  @Test def toCSVFileRealData2011 {
    val tournamentApi = new GenericTournamentAtpApi(5000)
    val atpMatchesLoader = new GenericATPMatchesLoader(tournamentApi, 16)
    val matches = atpMatchesLoader.loadMatches(2011)

    val matchDataFile = "./target/matches_data_file.csv"
    CSVATPMatchesLoader.toCSVFile(matches, matchDataFile)

    val matchData = Source.fromFile(matchDataFile)
    assertEquals(2688, matchData.reset().getLines().size)
    assertEquals("2011-01-02 00:00:00.000,Brisbane Australia ATP World Tour 250,HARD,2,Robin Soderling,Ryan Harrison,Robin Soderling,6-2; 6-4,66,39,52,28,50", matchData.reset().getLine(2))
    assertEquals("2011-01-10 00:00:00.000,Sydney Australia ATP World Tour 250,HARD,2,Frederico Gil,Jarkko Nieminen,Frederico Gil,6-2 RET,32,18,26,11,20", matchData.reset().getLine(123))
    assertEquals("2011-01-31 00:00:00.000,Johannesburg South Africa ATP World Tour 250,HARD,2,Simon Greul,Milos Raonic,Simon Greul,7-6(5); 6-4,106,58,83,52,75", matchData.reset().getLine(299))
    assertEquals("2011-11-20 00:00:00.000,Barclays ATP World Tour Finals Great Britain ATP World Tour,HARD,2,Roger Federer,Jo-Wilfried Tsonga,Roger Federer,6-3; 6-7(6); 6-3,139,69,98,62,98", matchData.reset().getLine(2688))
  }
  
   @Test @Ignore def toCSVFileRealData1995 {
    val tournamentApi = new GenericTournamentAtpApi(5000)
    val atpMatchesLoader = new GenericATPMatchesLoader(tournamentApi, 16)
    val matches = atpMatchesLoader.loadMatches(1995)

    val matchDataFile = "./target/matches_data_file.csv"
    CSVATPMatchesLoader.toCSVFile(matches, matchDataFile)

    val matchData = Source.fromFile(matchDataFile)
    assertEquals(2688, matchData.reset().getLines().size)
    assertEquals("2011-01-02 00:00:00.000,Brisbane Australia ATP World Tour 250,HARD,2,Robin Soderling,Ryan Harrison,Robin Soderling,6-2; 6-4,66,39,52,28,50", matchData.reset().getLine(2))
    assertEquals("2011-01-10 00:00:00.000,Sydney Australia ATP World Tour 250,HARD,2,Frederico Gil,Jarkko Nieminen,Frederico Gil,6-2 RET,32,18,26,11,20", matchData.reset().getLine(123))
    assertEquals("2011-01-31 00:00:00.000,Johannesburg South Africa ATP World Tour 250,HARD,2,Simon Greul,Milos Raonic,Simon Greul,7-6(5); 6-4,106,58,83,52,75", matchData.reset().getLine(299))
    assertEquals("2011-11-20 00:00:00.000,Barclays ATP World Tour Finals Great Britain ATP World Tour,HARD,2,Roger Federer,Jo-Wilfried Tsonga,Roger Federer,6-3; 6-7(6); 6-3,139,69,98,62,98", matchData.reset().getLine(2688))
  }

  private def matchComposite(eventTime: DateTime, playerAName: String = "Rafael Nadal", playerBName: String = "Marcos Daniel"): MatchComposite = {
    val tournament = Tournament(eventTime.toDate(), "Australian Open Australia Grand Slams", HARD, 3, "http://www.atpworldtour.com/Share/Event-Draws.aspx?e=580&y=2011")
    val tennisMatch = Match("6-0, 5-0 RET", "http://www.atpworldtour.com/Share/Match-Facts-Pop-Up.aspx?t=580&y=2011&r=1&p=N409")
    val matchFacts = MatchFacts(PlayerFacts(playerAName, 25, 35), PlayerFacts(playerBName, 2, 26), "Rafael Nadal", "R128", 56)
    MatchComposite(tournament, tennisMatch, matchFacts)
  }
}