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

    assertEquals(FirstServeFact(78, 31), firstServeFacts.playerFacts(0))
    assertEquals(FirstServeFact(74, 35), firstServeFacts.playerFacts(1))
    assertEquals(FirstServeFact(51, 14), firstServeFacts.playerFacts(70))
  }

  @Test def firstServeFacts_GRASS_2010 {

  }

  @Test def firstServeFacts_HARD_2010 {

  }

  @Test def firstServeFacts_CLAY_2009 {
    val firstServeFacts = atpApi.firstServeFacts(SurfaceEnum.CLAY, 2010)
    assertEquals(71, firstServeFacts.playerFacts.size)
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