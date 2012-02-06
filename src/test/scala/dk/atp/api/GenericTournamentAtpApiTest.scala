package dk.atp.api

import org.junit._
import Assert._

class GenericMatchAtpApiTest {

  @Test def parseMatches_2011_2_markets {
    
    val tournaments = GenericTournamentAtpApi.parseTournaments(2011,2)
    assertEquals(2,tournaments.size)
  }
}