package dk.atp.api

import org.junit._
import Assert._
import dk.atp.api.tournament._
import TournamentAtpApi._
import org.joda.time.DateTime
import dk.atp.api.domain.SurfaceEnum._
import GenericATPMatchesLoaderTest._

object GenericATPMatchesLoaderTest {
  private val tournamentApi = new GenericTournamentAtpApi(20000)
  private val atpMatchesLoader = new GenericATPMatchesLoader(tournamentApi, 16)

  private val matches2011 = atpMatchesLoader.loadMatches(2011)

}

class GenericATPMatchesLoaderTest {

  @Test def Nadal_vs_Marcos {

    val tournament = Tournament(DateTime.parse("2011-01-17").toDate(), "Australian Open Australia Grand Slams", HARD, 3, "http://www.atpworldtour.com/Share/Event-Draws.aspx?e=580&y=2011")
    val tennisMatch = Match("6-0, 5-0 RET", "http://www.atpworldtour.com/Share/Match-Facts-Pop-Up.aspx?t=0580&y=2011&r=1&p=N409")

    val nadalFacts = PlayerFacts("Rafael Nadal",
      4,
      3,
      19, 35,
      16, 19,
      9, 16,
      4, 4,
      5,
      25, 35)
    val marcosFacts = PlayerFacts("Marcos Daniel",
      0,
      0,
      17, 26,
      2, 17,
      0, 9,
      0, 6,
      6,
      2, 26)
    val matchFacts = MatchFacts(nadalFacts, marcosFacts, "Rafael Nadal", "R128", 47)
    assertEquals(tournament.toString, matches2011(0).tournament.toString)
    assertEquals(tennisMatch.toString, matches2011(0).tennisMatch.toString)
    assertEquals(matchFacts.toString, matches2011(0).matchFacts.toString)
    assertEquals(127, matches2011.groupBy(m => m.tournament.tournamentName)("Australian Open Australia Grand Slams").size)

  }

  @Test def Fish_vs_Falla {

    val tournament = Tournament(DateTime.parse("2011-02-21").toDate(), "Delray Beach FL, U.S.A. ATP World Tour 250", HARD, 2, "http://www.atpworldtour.com/Share/Event-Draws.aspx?e=499&y=2011")
    val tennisMatch = Match("6-1, 6-4", "http://www.atpworldtour.com/Share/Match-Facts-Pop-Up.aspx?t=0499&y=2011&r=5&p=F339")

    val fishFacts = PlayerFacts("Mardy Fish",
      6,
      2,
      35, 63,
      27, 35,
      17, 28,
      3, 3,
      9,
      44, 63)
    val fallaFacts = PlayerFacts("Alejandro Falla",
      1,
      4,
      36, 60,
      21, 36,
      12, 24,
      8, 11,
      8,
      33, 60)

    val matchFacts = MatchFacts(fishFacts, fallaFacts, "Mardy Fish", "Q", 86)
    assertEquals(tournament.toString, matches2011(1000).tournament.toString)
    assertEquals(tennisMatch.toString, matches2011(1000).tennisMatch.toString)
    assertEquals(matchFacts.toString, matches2011(1000).matchFacts.toString)
    assertEquals(31, matches2011.groupBy(m => m.tournament.tournamentName)("Delray Beach FL, U.S.A. ATP World Tour 250").size)

  }

  @Test def Giraldo_Melzer {

    val tournament = Tournament(DateTime.parse("2011-10-09").toDate(), "ATP World Tour Masters 1000 Shanghai Shanghai, China ATP World Tour Masters 1000", HARD, 2, "http://www.atpworldtour.com/Share/Event-Draws.aspx?e=5014&y=2011")
    val tennisMatch = Match("4-6, 6-4, 7-6(5)", "http://www.atpworldtour.com/Share/Match-Facts-Pop-Up.aspx?t=5014&y=2011&r=3&p=G725")

    val giraldoFacts = PlayerFacts("Santiago Giraldo",
      5, 3,
      78, 116,
      53, 78,
      22, 38,
      7, 9,
      16,
      75, 116)
    val melzerFacts = PlayerFacts("Jurgen Melzer",
      3, 2,
      64, 98,
      49, 64,
      17, 34,
      0, 2,
      16,
      66, 98)
    val matchFacts = MatchFacts(giraldoFacts, melzerFacts, "Santiago Giraldo", "R32", 144)

    assertEquals(tournament.toString, matches2011(2674).tournament.toString)
    assertEquals(tennisMatch.toString, matches2011(2674).tennisMatch.toString)
    assertEquals(matchFacts.toString, matches2011(2674).matchFacts.toString)
    assertEquals(63, matches2011.groupBy(m => m.tournament.tournamentName)("ATP World Tour Masters 1000 Shanghai Shanghai, China ATP World Tour Masters 1000").size)

  }

  @Test def loadMatches_2011 {
    assertEquals(2944, matches2011.size)
  }

}