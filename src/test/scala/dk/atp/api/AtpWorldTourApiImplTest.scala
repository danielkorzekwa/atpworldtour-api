package dk.atp.api

import org.junit._
import Assert._
import AtpWorldTourApi._

class AtpWorldTourApiImplTest {

  private val atpApi = new AtpWorldTourApiImpl();

  /**Tests for firstServeFacts.*/

  @Test def firstServeFacts_CLAY_2010 {
    val firstServeFacts = atpApi.firstServeFacts(SurfaceEnum.CLAY, 2010)
    assertEquals(71, firstServeFacts.playerFacts.size)

    assertEquals(FirstServeFact(1, "Potito Starace", 78, 31), firstServeFacts.playerFacts(0))
    assertEquals(FirstServeFact(2, "Juan Carlos Ferrero", 74, 35), firstServeFacts.playerFacts(1))
    assertEquals(FirstServeFact(71, "Marcos Baghdatis", 51, 14), firstServeFacts.playerFacts(70))
  }

  @Test def firstServeFacts_GRASS_2010 {
    val firstServeFacts = atpApi.firstServeFacts(SurfaceEnum.GRASS, 2010)
    assertEquals(127, firstServeFacts.playerFacts.size)

    assertEquals(FirstServeFact(1, "Julian Reister", 74, 2), firstServeFacts.playerFacts(0))
    assertEquals(FirstServeFact(2, "John Isner", 74, 2), firstServeFacts.playerFacts(1))
    assertEquals(FirstServeFact(127, "Ergun Zorlu", 0, 2), firstServeFacts.playerFacts(126))
  }

  @Test def firstServeFacts_HARD_2010 {
    val firstServeFacts = atpApi.firstServeFacts(SurfaceEnum.HARD, 2010)
    assertEquals(63, firstServeFacts.playerFacts.size)

    assertEquals(FirstServeFact(1, "Jarkko Nieminen", 71, 40), firstServeFacts.playerFacts(0))
    assertEquals(FirstServeFact(2, "John Isner", 69, 42), firstServeFacts.playerFacts(1))
    assertEquals(FirstServeFact(63, "Marcos Baghdatis", 52, 53), firstServeFacts.playerFacts(62))
  }

  @Test def firstServeFacts_CLAY_2009 {
    val firstServeFacts = atpApi.firstServeFacts(SurfaceEnum.CLAY, 2009)
    assertEquals(78, firstServeFacts.playerFacts.size)

    assertEquals(FirstServeFact(1, "Potito Starace", 75, 26), firstServeFacts.playerFacts(0))
    assertEquals(FirstServeFact(2, "Fernando Verdasco", 74, 20), firstServeFacts.playerFacts(1))
    assertEquals(FirstServeFact(78, "David Nalbandian", 51, 11), firstServeFacts.playerFacts(77))
  }

  /**Tests for pointWonFacts.*/

  @Test def pointWonFacts_CLAY {

  }

  @Test def pointWonFacts_GRASS {

  }

  @Test def pointWonFacts_HARD {

  }

  @Test def pointWonFacts_FIRST_SERVE_POINTS_WON {

  }

  @Test def pointWonFacts_SECOND_SERVE_POINTS_WON {

  }

  @Test def pointWonFacts_FIRST_SERVE_RETURN_POINTS_WON {

  }

  @Test def pointWonFacts_SECOND_SERVE_RETURN_POINTS_WON {

  }
}