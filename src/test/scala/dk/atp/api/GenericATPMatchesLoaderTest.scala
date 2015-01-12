package dk.atp.api

import org.junit._
import Assert._
import dk.atp.api.tournament._
import TournamentAtpApi._
import org.joda.time.DateTime
import dk.atp.api.domain.SurfaceEnum._
import GenericATPMatchesLoaderTest._
import dk.atp.api.domain.Tournament
import dk.atp.api.domain.PlayerFacts
import dk.atp.api.domain.TennisMatch

object GenericATPMatchesLoaderTest {
  private val tournamentApi = new GenericTournamentAtpApi(20000)
  private val atpMatchesLoader = new GenericATPMatchesLoader(tournamentApi, 16)

}

class GenericATPMatchesLoaderTest {

  @Test def Nadal_vs_Marcos {

    val matches2011 = atpMatchesLoader.loadMatches(2011)

    val tournament = Tournament(DateTime.parse("2011-01-17").toDate(), "Australian Open Australia Grand Slams", HARD, 3)

    val nadalFacts = PlayerFacts(
      4,
      3,
      19, 35,
      16, 19,
      9, 16,
      4, 4,
      5,
      25, 35)
    val marcosFacts = PlayerFacts(
      0,
      0,
      17, 26,
      2, 17,
      0, 9,
      0, 6,
      6,
      2, 26)

    val tennisMatch = TennisMatch(tournament, "Rafael Nadal", "Marcos Daniel", "6-0, 5-0 RET", "Rafael Nadal", "R128", 47, nadalFacts, marcosFacts)
    assertEquals(tournament.toString, matches2011(0).tournament.toString)
    assertEquals(tennisMatch.toString, matches2011(0).toString)
    assertEquals(127, matches2011.groupBy(m => m.tournament.tournamentName)("Australian Open Australia Grand Slams").size)

  }

  @Test def Fish_vs_Falla {

    val matches2011 = atpMatchesLoader.loadMatches(2011)

    val tournament = Tournament(DateTime.parse("2011-02-21").toDate(), "Delray Beach FL, U.S.A. ATP World Tour 250", HARD, 2)

    val fishFacts = PlayerFacts(
      6,
      2,
      35, 63,
      27, 35,
      17, 28,
      3, 3,
      9,
      44, 63)
    val fallaFacts = PlayerFacts(
      1,
      4,
      36, 60,
      21, 36,
      12, 24,
      8, 11,
      8,
      33, 60)

    val tennisMatch = TennisMatch(tournament, "Mardy Fish", "Alejandro Falla", "6-1, 6-4", "Mardy Fish", "Q", 86, fishFacts, fallaFacts)

    assertEquals(tournament.toString, matches2011(1000).tournament.toString)
    assertEquals(tennisMatch.toString, matches2011(1000).toString)
    assertEquals(31, matches2011.groupBy(m => m.tournament.tournamentName)("Delray Beach FL, U.S.A. ATP World Tour 250").size)

  }

  @Test def Giraldo_Melzer {

    val matches2011 = atpMatchesLoader.loadMatches(2011)

    val tournament = Tournament(DateTime.parse("2011-10-09").toDate(), "ATP World Tour Masters 1000 Shanghai Shanghai, China ATP World Tour Masters 1000", HARD, 2)

    val giraldoFacts = PlayerFacts(
      5, 3,
      78, 116,
      53, 78,
      22, 38,
      7, 9,
      16,
      75, 116)
    val melzerFacts = PlayerFacts(
      3, 2,
      64, 98,
      49, 64,
      17, 34,
      0, 2,
      16,
      66, 98)

    val tennisMatch = TennisMatch(tournament, "Santiago Giraldo", "Jurgen Melzer", "4-6, 6-4, 7-6(5)", "Santiago Giraldo", "R32", 144, giraldoFacts, melzerFacts)

    assertEquals(tournament.toString, matches2011(2674).tournament.toString)
    assertEquals(tennisMatch.toString, matches2011(2674).toString)
    assertEquals(63, matches2011.groupBy(m => m.tournament.tournamentName)("ATP World Tour Masters 1000 Shanghai Shanghai, China ATP World Tour Masters 1000").size)

  }

  @Test def loadMatches_2011 {
    val matches2011 = atpMatchesLoader.loadMatches(2011)
    assertEquals(2944, matches2011.size)
  }

  @Test def loadMatches_2015 {
    val matches2015 = atpMatchesLoader.loadMatches(2015)
   // assertEquals(2944, matches2015.size)
  }

}