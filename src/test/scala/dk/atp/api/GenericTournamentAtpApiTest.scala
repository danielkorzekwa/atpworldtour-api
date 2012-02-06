package dk.atp.api

import org.junit._
import Assert._
import TournamentAtpApi._
import java.util.Date
import AtpWorldTourApi.SurfaceEnum._
import org.joda.time.DateTime

class GenericMatchAtpApiTest {

  val api = new GenericTournamentAtpApi(5000)

  @Test @Ignore def parseMatches_2011_2_markets {

    val tournaments = api.parseTournaments(2011, 10000)
    assertEquals(67, tournaments.size)

    assertEquals(Tournament(DateTime.parse("2011-01-17").toDate(), "", HARD, 5, Nil), tournaments(0))
    assertEquals(Tournament(new Date(0), "", HARD, 1, Nil), tournaments(1))

    assertEquals(Tournament(new Date(0), "", HARD, 1, Nil), tournaments(5))
    assertEquals(Tournament(new Date(0), "", HARD, 1, Nil), tournaments(6))
  }
}