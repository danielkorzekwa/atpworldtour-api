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

    assertEquals("event_time,event_name,surface,num_of_sets,playerA,playerB,winner,score,round,duration_minutes,a_aces,a_double_faults,a_first_serve_hits,a_first_serve_total,a_first_serve_won,a_first_serve_played,a_second_serve_won,a_second_serve_played,a_break_points_saved,a_break_points_total,a_service_games,a_service_points_won,a_service_points_total,b_aces,b_double_faults,b_first_serve_hits,b_first_serve_total,b_first_serve_won,b_first_serve_played,b_second_serve_won,b_second_serve_played,b_break_points_saved,b_break_points_total,b_service_games,b_service_points_won,b_service_points_total", matchData.reset().getLines().toIndexedSeq(0))
    assertEquals("2011-01-17 00:00:00.000,Australian Open Australia Grand Slams,HARD,3,Rafael Nadal,Marcos Daniel,Rafael Nadal,6-0; 5-0 RET,R128,56,1,2,3,4,5,6,7,8,9,10,11,25,35,12,13,14,15,16,17,18,19,20,21,22,2,26", matchData.reset().getLines().toIndexedSeq(1))
    assertEquals("2012-02-12 10:20:30.000,Australian Open Australia Grand Slams,HARD,3,Rafael Nadal2,Marcos Daniel2,Rafael Nadal,6-0; 5-0 RET,R128,56,1,2,3,4,5,6,7,8,9,10,11,25,35,12,13,14,15,16,17,18,19,20,21,22,2,26", matchData.reset().getLines().toIndexedSeq(2))
    assertEquals("2012-05-19 00:00:00.000,Australian Open Australia Grand Slams,HARD,3,Rafael Nadal3,Marcos Daniel3,Rafael Nadal,6-0; 5-0 RET,R128,56,1,2,3,4,5,6,7,8,9,10,11,25,35,12,13,14,15,16,17,18,19,20,21,22,2,26", matchData.reset().getLines().toIndexedSeq(3))
  }

  @Test def toCSVFileRealData2011 {
    val atpMatchesLoader = CSVATPMatchesLoader.fromCSVFile("./src/test/resources/match_data_2010_2011.csv")
    val matches = atpMatchesLoader.loadMatches(2011)

    val matchDataFile = "./target/matches_data_file.csv"
    CSVATPMatchesLoader.toCSVFile(matches, matchDataFile)

    val matchData = Source.fromFile(matchDataFile)
    assertEquals(2945, matchData.reset().getLines().size)
    assertEquals("2011-01-02 00:00:00.000,Brisbane Australia ATP World Tour 250,HARD,2,Robin Soderling,Ryan Harrison,Robin Soderling,6-2; 6-4,R32,66,8,3,32,52,26,32,13,20,1,1,9,39,52,4,1,27,50,16,27,12,23,1,4,9,28,50", matchData.reset().getLines().toIndexedSeq(1))
    assertEquals("2011-01-10 00:00:00.000,Sydney Australia ATP World Tour 250,HARD,2,Frederico Gil,Jarkko Nieminen,Frederico Gil,6-2 RET,R32,32,1,0,19,26,14,19,4,7,4,4,4,18,26,0,0,12,20,6,12,5,8,0,2,4,11,20", matchData.reset().getLines().toIndexedSeq(127))
    assertEquals("2011-01-31 00:00:00.000,Johannesburg South Africa ATP World Tour 250,HARD,2,Frank Dancevic,Fritz Wolmarans,Frank Dancevic,7-6(4); 6-3,R16,96,6,5,44,75,35,44,14,31,1,2,11,49,75,9,3,42,64,28,42,13,22,5,7,10,41,64", matchData.reset().getLines().toIndexedSeq(299))
    assertEquals("2011-10-09 00:00:00.000,ATP World Tour Masters 1000 Shanghai Shanghai; China ATP World Tour Masters 1000,HARD,2,Andy Murray,Stanislas Wawrinka,Andy Murray,6-4; 3-6; 6-3,R16,132,4,3,55,86,40,55,13,31,4,7,14,53,86,5,2,44,78,30,44,17,34,1,5,14,47,78", matchData.reset().getLines().toIndexedSeq(2688))
  }

  @Test def toCSVFileRealData1995 {
    val atpMatchesLoader = CSVATPMatchesLoader.fromCSVFile("./src/test/resources/match_data_1995.csv")
    val matches = atpMatchesLoader.loadMatches(1995)

    val matchDataFile = "./target/matches_data_file.csv"
    CSVATPMatchesLoader.toCSVFile(matches, matchDataFile)

    val matchData = Source.fromFile(matchDataFile)
    assertEquals(3645, matchData.reset().getLines().size)
    assertEquals("1995-01-02 00:00:00.000,Adelaide Australia ATP World Tour,HARD,2,Henrik Holm,Cedric Pioline,Henrik Holm,6-2; 6-2,R32,70,4,8,28,59,22,28,17,31,7,7,8,39,59,1,5,30,55,19,30,9,25,7,11,8,28,55", matchData.reset().getLines().toIndexedSeq(2))
    assertEquals("1995-01-09 00:00:00.000,Jakarta Indonesia ATP World Tour,HARD,2,Paul Haarhuis,Kenneth Carlsen,Paul Haarhuis,3-6; 6-3; 6-3,S,117,2,2,47,77,33,47,21,30,5,6,13,54,77,10,6,61,99,43,61,16,38,9,12,14,59,99", matchData.reset().getLines().toIndexedSeq(123))
    assertEquals("1995-02-06 00:00:00.000,Dubai U.A.E. ATP World Tour,HARD,2,Javier Sanchez,Sergi Bruguera,Javier Sanchez,W/O,R16,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0", matchData.reset().getLines().toIndexedSeq(299))
    assertEquals("1995-10-23 00:00:00.000,ATP Masters Series Essen Germany ATP World Tour,HARD,2,Thomas Enqvist,Todd Martin,Thomas Enqvist,6-3; 6-4,R32,66,2,3,25,54,23,25,19,29,2,2,10,42,54,9,4,33,51,26,33,5,18,1,3,9,31,51", matchData.reset().getLines().toIndexedSeq(3383))
  }

  /**Tests for CSVATPMatchesLoader.fromCSVFile*/
  @Test def fromCSVFile {
    val csvMatchLoader = CSVATPMatchesLoader.fromCSVFile("./src/test/resources/match_data_2010_2011.csv")

    assertEquals(0, csvMatchLoader.loadMatches(2009).size)
    assertEquals(2944, csvMatchLoader.loadMatches(2010).size)
    assertEquals(2944, csvMatchLoader.loadMatches(2011).size)
    assertEquals(0, csvMatchLoader.loadMatches(2012).size)

    val tournament = Tournament(DateTime.parse("2010-01-03T00:00:00").toDate(), "Brisbane Australia ATP World Tour 250", HARD, 2)

    val serraFacts = PlayerFacts(
      9,
      1,
      67, 107,
      46, 67,
      28, 40,
      2, 5,
      18,
      74, 107)
    val reisterFacts = PlayerFacts(
      7,
      1,
      105, 143,
      69, 105,
      19, 38,
      5, 9,
      18,
      88, 143)

    val tennisMatch = TennisMatch(tournament, "Florent Serra", "Julian Reister", "6-7(3); 7-6(5); 7-5", "Florent Serra", "R32", 160, serraFacts, reisterFacts)

    assertEquals(tournament.toString, csvMatchLoader.loadMatches(2010)(10).tournament.toString)
    assertEquals(tennisMatch.toString, csvMatchLoader.loadMatches(2010)(10).toString)

    /**Check numOFSets*/
    val tournament2 = Tournament(DateTime.parse("2010-01-18T00:00:00").toDate(), "Australian Open Australia Grand Slams", HARD, 3)
    assertEquals(tournament2.toString, csvMatchLoader.loadMatches(2010)(155).tournament.toString)
  }

  private def matchComposite(eventTime: DateTime, playerAName: String = "Rafael Nadal", playerBName: String = "Marcos Daniel"): TennisMatch = {
    val tournament = Tournament(eventTime.toDate(), "Australian Open Australia Grand Slams", HARD, 3)

    val playerAFacts = PlayerFacts(
      1,
      2,
      3, 4,
      5, 6,
      7, 8,
      9, 10,
      11,
      25, 35)
    val playerBFacts = PlayerFacts(
      12,
      13,
      14, 15,
      16, 17,
      18, 19,
      20, 21,
      22,
      2, 26)

    val tennisMatch = TennisMatch(tournament, playerAName, playerBName, "6-0, 5-0 RET", "Rafael Nadal", "R128", 56, playerAFacts, playerBFacts)
    tennisMatch

  }
}