package dk.atp.api

import org.junit._
import Assert._
import TournamentAtpApi._
import java.util.Date
import AtpWorldTourApi.SurfaceEnum._
import org.joda.time.DateTime

class GenericMatchAtpApiTest {

  val api = new GenericTournamentAtpApi(10000)

  @Test def parseMatches_2011 {

    val tournaments = api.parseTournaments(2011)
    assertEquals(67, tournaments.size)

    assertEquals(Tournament(DateTime.parse("2011-01-17").toDate(), "Australian Open Australia Grand Slams", HARD, 3, Nil), tournaments(0).copy(matches = Nil))
    assertEquals(Tournament(DateTime.parse("2011-05-22").toDate(), "Roland Garros France Grand Slams", CLAY, 3, Nil), tournaments(1).copy(matches = Nil))
    assertEquals(Tournament(DateTime.parse("2011-06-20").toDate(), "Wimbledon Great Britain Grand Slams", GRASS, 3, Nil), tournaments(2).copy(matches = Nil))

    assertEquals(Tournament(DateTime.parse("2011-01-03").toDate(), "Chennai India ATP World Tour 250", HARD, 2, Nil), tournaments(5).copy(matches = Nil))
    assertEquals(Tournament(DateTime.parse("2011-01-10").toDate(), "Auckland New Zealand ATP World Tour 250", HARD, 2, Nil), tournaments(7).copy(matches = Nil))

    /**Check matches.*/
    assertEquals(127, tournaments(0).matches.size)
    assertEquals(126, tournaments(1).matches.size)
    assertEquals(127, tournaments(2).matches.size)
    assertEquals(31, tournaments(5).matches.size)
    assertEquals(27, tournaments(7).matches.size)

    assertEquals(Match(DateTime.parse("2011-01-17").toDate(), List("Rafael Nadal", "Marcos Daniel"), "Rafael Nadal", "6-0, 5-0 RET"), tournaments(0).matches(0))
    assertEquals(Match(DateTime.parse("2011-01-17").toDate(), List("Novak Djokovic","Andy Murray"), "Novak Djokovic", "6-4, 6-2, 6-3"), tournaments(0).matches(126))
  }
}