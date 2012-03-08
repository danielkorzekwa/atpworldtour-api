package dk.atp.api

import org.junit._
import Assert._
import dk.atp.api.tournament._
import TournamentAtpApi._
import org.joda.time.DateTime
import dk.atp.api.domain.SurfaceEnum._

class GenericATPMatchesLoaderTest {

  private val tournamentApi = new GenericTournamentAtpApi(10000)
  private val atpMatchesLoader = new GenericATPMatchesLoader(tournamentApi, 16)

  @Test def loadMatches {
    val matches = atpMatchesLoader.loadMatches(2011)
    assertEquals(2957, matches.size)

    val tournament1 = Tournament(DateTime.parse("2011-01-17").toDate(), "Australian Open Australia Grand Slams", HARD, 3, "http://www.atpworldtour.com/Share/Event-Draws.aspx?e=580&y=2011")
    val match1 = Match("6-0, 5-0 RET", "http://www.atpworldtour.com/Share/Match-Facts-Pop-Up.aspx?t=580&y=2011&r=1&p=N409")
    val matchFacts1 = MatchFacts(PlayerFacts("Rafael Nadal", 25, 35), PlayerFacts("Marcos Daniel", 2, 26), "Rafael Nadal", "R128", 47)
    assertEquals(tournament1.toString, matches(0).tournament.toString)
    assertEquals(match1.toString, matches(0).tennisMatch.toString)
    assertEquals(matchFacts1.toString, matches(0).matchFacts.toString)
    assertEquals(127, matches.groupBy(m => m.tournament.tournamentName)("Australian Open Australia Grand Slams").size)

    val tournament2 = Tournament(DateTime.parse("2011-02-21").toDate(), "Delray Beach FL, U.S.A. ATP World Tour 250", HARD, 2, "http://www.atpworldtour.com/Share/Event-Draws.aspx?e=499&y=2011")
    val match2 = Match("6-1, 6-4", "http://www.atpworldtour.com/Share/Match-Facts-Pop-Up.aspx?t=499&y=2011&r=5&p=F339")
    val matchFacts2 = MatchFacts(PlayerFacts("Mardy Fish", 44, 63), PlayerFacts("Alejandro Falla", 33, 60), "Mardy Fish", "Q", 86)
    assertEquals(tournament2.toString, matches(1000).tournament.toString)
    assertEquals(match2.toString, matches(1000).tennisMatch.toString)
    assertEquals(matchFacts2.toString, matches(1000).matchFacts.toString)
    assertEquals(31, matches.groupBy(m => m.tournament.tournamentName)("Delray Beach FL, U.S.A. ATP World Tour 250").size)

    val tournament3 = Tournament(DateTime.parse("2011-10-09").toDate(), "ATP World Tour Masters 1000 Shanghai Shanghai, China ATP World Tour Masters 1000", HARD, 2, "http://www.atpworldtour.com/Share/Event-Draws.aspx?e=5014&y=2011")
    val match3 = Match("4-6, 6-4, 7-6(5)", "http://www.atpworldtour.com/Share/Match-Facts-Pop-Up.aspx?t=5014&y=2011&r=3&p=G725")
    val matchFacts3 = MatchFacts(PlayerFacts("Santiago Giraldo", 75, 116), PlayerFacts("Jurgen Melzer", 66, 98), "Santiago Giraldo", "R32", 144)
    assertEquals(tournament3.toString, matches(2686).tournament.toString)
    assertEquals(match3.toString, matches(2686).tennisMatch.toString)
    assertEquals(matchFacts3.toString, matches(2686).matchFacts.toString)
    assertEquals(63, matches.groupBy(m => m.tournament.tournamentName)("ATP World Tour Masters 1000 Shanghai Shanghai, China ATP World Tour Masters 1000").size)

  }

  @Test def loadMatches_2007 {
    val matches = new GenericATPMatchesLoader(tournamentApi, 16).loadMatches(2007)
    assertEquals(2864, matches.size)
  }
 
}