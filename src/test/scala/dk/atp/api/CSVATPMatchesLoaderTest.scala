package dk.atp.api

import org.junit._
import Assert._
import domain._
import dk.atp.api.tournament.TournamentAtpApi._
import org.joda.time._
import dk.atp.api.domain.SurfaceEnum._
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
    assertEquals("2011-01-17 00:00:00.000,Australian Open Australia Grand Slams,HARD,3,Rafael Nadal,Marcos Daniel,Rafael Nadal,6-0; 5-0 RET,R128,56,25,35,2,26", matchData.reset().getLine(2))
    assertEquals("2012-02-12 10:20:30.000,Australian Open Australia Grand Slams,HARD,3,Rafael Nadal2,Marcos Daniel2,Rafael Nadal,6-0; 5-0 RET,R128,56,25,35,2,26", matchData.reset().getLine(3))
    assertEquals("2012-05-19 00:00:00.000,Australian Open Australia Grand Slams,HARD,3,Rafael Nadal3,Marcos Daniel3,Rafael Nadal,6-0; 5-0 RET,R128,56,25,35,2,26", matchData.reset().getLine(4))
  }

  @Test def toCSVFileRealData2011 {
    val atpMatchesLoader = CSVATPMatchesLoader.fromCSVFile("./src/test/resources/match_data_2010_2011.csv")
    val matches = atpMatchesLoader.loadMatches(2011)

    val matchDataFile = "./target/matches_data_file.csv"
    CSVATPMatchesLoader.toCSVFile(matches, matchDataFile)

    val matchData = Source.fromFile(matchDataFile)
    assertEquals(2688, matchData.reset().getLines().size)
    assertEquals("2011-01-02 00:00:00.000,Brisbane Australia ATP World Tour 250,HARD,2,Robin Soderling,Ryan Harrison,Robin Soderling,6-2; 6-4,R32,66,39,52,28,50", matchData.reset().getLine(2))
    assertEquals("2011-01-10 00:00:00.000,Sydney Australia ATP World Tour 250,HARD,2,Frederico Gil,Jarkko Nieminen,Frederico Gil,6-2 RET,R32,32,18,26,11,20", matchData.reset().getLine(123))
    assertEquals("2011-01-31 00:00:00.000,Johannesburg South Africa ATP World Tour 250,HARD,2,Simon Greul,Milos Raonic,Simon Greul,7-6(5); 6-4,R16,106,58,83,52,75", matchData.reset().getLine(299))
    assertEquals("2011-11-20 00:00:00.000,Barclays ATP World Tour Finals Great Britain ATP World Tour,HARD,2,Roger Federer,Jo-Wilfried Tsonga,Roger Federer,6-3; 6-7(6); 6-3,F,139,69,98,62,98", matchData.reset().getLine(2688))
  }

  @Test def toCSVFileRealData1995 {
    val atpMatchesLoader = CSVATPMatchesLoader.fromCSVFile("./src/test/resources/match_data_1995.csv")
    val matches = atpMatchesLoader.loadMatches(1995)

    val matchDataFile = "./target/matches_data_file.csv"
    CSVATPMatchesLoader.toCSVFile(matches, matchDataFile)

    val matchData = Source.fromFile(matchDataFile)
    assertEquals(3383, matchData.reset().getLines().size)
    assertEquals("1995-01-02 00:00:00.000,Adelaide Australia ATP World Tour,HARD,2,Yevgeny Kafelnikov,Marcos Ondruska,Yevgeny Kafelnikov,7-6(5); 6-1,R32,82,40,57,42,75", matchData.reset().getLine(2))
    assertEquals("1995-01-09 00:00:00.000,Jakarta Indonesia ATP World Tour,HARD,2,Paul Haarhuis,Radomir Vasek,Paul Haarhuis,7-5; 7-5,F,99,40,65,47,88", matchData.reset().getLine(123))
    assertEquals("1995-02-06 00:00:00.000,Dubai U.A.E. ATP World Tour,HARD,2,Pat Cash,Albert Costa,Pat Cash,6-3; 4-6; 6-3,R16,118,58,88,57,95", matchData.reset().getLine(299))
    assertEquals("1995-11-14 00:00:00.000,ATP Tour World Championship Germany ATP World Tour,HARD,2,Boris Becker,Michael Chang,Boris Becker,7-6(3); 6-0; 7-6(5),F,137,70,96,62,104", matchData.reset().getLine(3383))
  }

  /**Tests for CSVATPMatchesLoader.fromCSVFile*/
  @Test def fromCSVFile {
    val csvMatchLoader = CSVATPMatchesLoader.fromCSVFile("./src/test/resources/match_data_2010_2011.csv")

    assertEquals(0, csvMatchLoader.loadMatches(2009).size)
    assertEquals(2689, csvMatchLoader.loadMatches(2010).size)
    assertEquals(2687, csvMatchLoader.loadMatches(2011).size)
    assertEquals(0, csvMatchLoader.loadMatches(2012).size)

    val tournament = Tournament(DateTime.parse("2010-01-03T00:00:00").toDate(), "Brisbane Australia ATP World Tour 250", HARD, 2, "n/a")
    val tennisMatch = Match("6-7(3); 7-6(5); 7-5", "n/a")
    val matchFacts = MatchFacts(PlayerFacts("Florent Serra", 74, 107), PlayerFacts("Julian Reister", 88, 143), "Florent Serra", "R32", 160)

    assertEquals(tournament.toString, csvMatchLoader.loadMatches(2010)(10).tournament.toString)
    assertEquals(tennisMatch.toString, csvMatchLoader.loadMatches(2010)(10).tennisMatch.toString)
    assertEquals(matchFacts.toString, csvMatchLoader.loadMatches(2010)(10).matchFacts.toString)
    
    /**Check numOFSets*/
    val tournament2 = Tournament(DateTime.parse("2010-01-18T00:00:00").toDate(), "Australian Open Australia Grand Slams", HARD, 3, "n/a")
    assertEquals(tournament2.toString, csvMatchLoader.loadMatches(2010)(150).tournament.toString)
  }

  private def matchComposite(eventTime: DateTime, playerAName: String = "Rafael Nadal", playerBName: String = "Marcos Daniel"): MatchComposite = {
    val tournament = Tournament(eventTime.toDate(), "Australian Open Australia Grand Slams", HARD, 3, "http://www.atpworldtour.com/Share/Event-Draws.aspx?e=580&y=2011")
    val tennisMatch = Match("6-0, 5-0 RET", "http://www.atpworldtour.com/Share/Match-Facts-Pop-Up.aspx?t=580&y=2011&r=1&p=N409")
    val matchFacts = MatchFacts(PlayerFacts(playerAName, 25, 35), PlayerFacts(playerBName, 2, 26), "Rafael Nadal", "R128", 56)
    MatchComposite(tournament, tennisMatch, matchFacts)
  }
}