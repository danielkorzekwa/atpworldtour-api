package dk.atp.api

import org.junit._
import Assert._
import dk.atp.api.tournament._
import TournamentAtpApi._
import org.joda.time.DateTime
import AtpWorldTourApi.SurfaceEnum._

class GenericATPMatchesLoaderTest {

  private val tournamentApi = new GenericTournamentAtpApi(5000)
  private val atpMatchesLoader = new GenericATPMatchesLoader(tournamentApi, 16)

  @Test def loadMatches {
    val matches = atpMatchesLoader.loadMatches(2011)
    assertEquals(2687, matches.size)

    val tournament1 = Tournament(DateTime.parse("2011-01-17").toDate(), "Australian Open Australia Grand Slams", HARD, 3, "http://www.atpworldtour.com/Share/Event-Draws.aspx?e=580&y=2011")
    val match1 = Match("6-0, 5-0 RET", "http://www.atpworldtour.com/Share/Match-Facts-Pop-Up.aspx?t=580&y=2011&r=1&p=N409")
    val matchFacts1 = MatchFacts(PlayerFacts("Rafael Nadal", 25, 35), PlayerFacts("Marcos Daniel", 2, 26), "Rafael Nadal", "R128", 47)
    assertEquals(tournament1.toString, matches(0).tournament.toString)
    assertEquals(match1.toString, matches(0).tennisMatch.toString)
    assertEquals(matchFacts1.toString, matches(0).matchFacts.toString)

    val tournament2 = Tournament(DateTime.parse("2011-02-21").toDate(), "Dubai U.A.E. ATP World Tour 500", HARD, 2, "http://www.atpworldtour.com/Share/Event-Draws.aspx?e=495&y=2011")
    val match2 = Match("6-0, 4-6, 6-2", "http://www.atpworldtour.com/Share/Match-Facts-Pop-Up.aspx?t=495&y=2011&r=4&p=P624")
    val matchFacts2 = MatchFacts(PlayerFacts("Philipp Petzschner", 49, 68), PlayerFacts("Philipp Kohlschreiber", 39, 74), "Philipp Petzschner", "R16", 95)
    assertEquals(tournament2.toString, matches(1000).tournament.toString)
    assertEquals(match2.toString, matches(1000).tennisMatch.toString)
    assertEquals(matchFacts2.toString, matches(1000).matchFacts.toString)

    val tournament3 = Tournament(DateTime.parse("2011-11-20").toDate(), "Barclays ATP World Tour Finals Great Britain ATP World Tour", HARD, 2, "http://www.atpworldtour.com/Share/Event-Draws.aspx?e=605&y=2011")
    val match3 = Match("6-3, 6-7(6), 6-3", "http://www.atpworldtour.com/Share/Match-Facts-Pop-Up.aspx?t=605&y=2011&r=7&p=F324")
    val matchFacts3 = MatchFacts(PlayerFacts("Roger Federer", 69, 98), PlayerFacts("Jo-Wilfried Tsonga", 62, 98), "Roger Federer", "F", 139)
    assertEquals(tournament3.toString, matches(2686).tournament.toString)
    assertEquals(match3.toString, matches(2686).tennisMatch.toString)
    assertEquals(matchFacts3.toString, matches(2686).matchFacts.toString)
  }
  
}