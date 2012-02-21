package dk.atp.api.tournament

import org.junit._
import Assert._
import TournamentAtpApi._
import java.util.Date
import dk.atp.api.AtpWorldTourApi.SurfaceEnum._
import org.joda.time.DateTime

class GenericMatchAtpApiTest {

  val api = new GenericTournamentAtpApi(10000)

  @Test def parseTournaments_2011 {

    val tournaments = api.parseTournaments(2011)
    assertEquals(67, tournaments.size)

    assertEquals(Tournament(DateTime.parse("2011-01-17").toDate(), "Australian Open Australia Grand Slams", HARD, 3, "http://www.atpworldtour.com/Share/Event-Draws.aspx?e=580&y=2011").toString, tournaments(0).toString)
    assertEquals(Tournament(DateTime.parse("2011-05-22").toDate(), "Roland Garros France Grand Slams", CLAY, 3, "http://www.atpworldtour.com/Share/Event-Draws.aspx?e=520&y=2011").toString, tournaments(1).toString)
    assertEquals(Tournament(DateTime.parse("2011-06-20").toDate(), "Wimbledon Great Britain Grand Slams", GRASS, 3, "http://www.atpworldtour.com/Share/Event-Draws.aspx?e=540&y=2011").toString, tournaments(2).toString)

    assertEquals(Tournament(DateTime.parse("2011-01-03").toDate(), "Chennai India ATP World Tour 250", HARD, 2, "http://www.atpworldtour.com/Share/Event-Draws.aspx?e=891&y=2011").toString, tournaments(5).toString)
    assertEquals(Tournament(DateTime.parse("2011-01-10").toDate(), "Auckland New Zealand ATP World Tour 250", HARD, 2, "http://www.atpworldtour.com/Share/Event-Draws.aspx?e=301&y=2011").toString, tournaments(7).toString)

     assertEquals(Tournament(DateTime.parse("2011-11-20").toDate(), "Barclays ATP World Tour Doubles Finals Great Britain ATP World Tour", HARD, 2, "").toString, tournaments(65).toString)

  }
  
   @Test def parseTournaments_2010 {

    val tournaments = api.parseTournaments(2010)
    assertEquals(67, tournaments.size)

    assertEquals(Tournament(DateTime.parse("2010-01-18").toDate(), "Australian Open Australia Grand Slams", HARD, 3, "http://www.atpworldtour.com/Share/Event-Draws.aspx?e=580&y=2010").toString, tournaments(0).toString)
    assertEquals(Tournament(DateTime.parse("2010-02-08").toDate(), "Costa Do Sauipe Brazil ATP World Tour 250", CLAY, 2, "http://www.atpworldtour.com/Share/Event-Draws.aspx?e=533&y=2010").toString, tournaments(12).toString)
   
  }

  @Test def parseTournamentMatches {
    val tournamentUrl = "http://www.atpworldtour.com/Share/Event-Draws.aspx?e=580&y=2011"
    val matches = api.parseTournament(tournamentUrl)

    assertEquals(127, matches.size)

    assertEquals(Match("6-0, 5-0 RET", "http://www.atpworldtour.com/Share/Match-Facts-Pop-Up.aspx?t=580&y=2011&r=1&p=N409").toString(), matches(0).toString())
    assertEquals(Match("6-4, 6-0, 6-1", "http://www.atpworldtour.com/Share/Match-Facts-Pop-Up.aspx?t=580&y=2011&r=2&p=M680").toString(), matches(94).toString())

    assertEquals(Match("6-4, 6-2, 6-3", "http://www.atpworldtour.com/Share/Match-Facts-Pop-Up.aspx?t=580&y=2011&r=7&p=D643").toString(), matches(126).toString())

  }

  @Test def parseMatchFacts {

    val matchFactsUrl = "http://www.atpworldtour.com/Share/Match-Facts-Pop-Up.aspx?t=580&y=2011&r=1&p=N409"
    val matchFacts = api.parseMatchFacts(matchFactsUrl)

    assertEquals(PlayerFacts("Rafael Nadal", 25, 35).toString, matchFacts.playerAFacts.toString)
    assertEquals(PlayerFacts("Marcos Daniel", 2, 26).toString, matchFacts.playerBFacts.toString)

    assertEquals("Rafael Nadal", matchFacts.winner)
    assertEquals("R128", matchFacts.round)
    assertEquals(47, matchFacts.durationMinutes)

  }
}