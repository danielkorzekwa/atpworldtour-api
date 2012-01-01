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
    assertEquals(62.267, firstServeFacts.firstServeAvgPct(), 0.001)

    assertEquals(FirstServeFact(1, "Potito Starace", 78, 31), firstServeFacts.playerFacts(0))
    assertEquals(FirstServeFact(2, "Juan Carlos Ferrero", 74, 35), firstServeFacts.playerFacts(1))
    assertEquals(FirstServeFact(71, "Marcos Baghdatis", 51, 14), firstServeFacts.playerFacts(70))
  }

  @Test def firstServeFacts_GRASS_2010 {
    val firstServeFacts = atpApi.firstServeFacts(SurfaceEnum.GRASS, 2010)
    assertEquals(127, firstServeFacts.playerFacts.size)
    assertEquals(61.748, firstServeFacts.firstServeAvgPct(), 0.001)

    assertEquals(FirstServeFact(1, "Julian Reister", 74, 2), firstServeFacts.playerFacts(0))
    assertEquals(FirstServeFact(2, "John Isner", 74, 2), firstServeFacts.playerFacts(1))
    assertEquals(FirstServeFact(127, "Ergun Zorlu", 0, 2), firstServeFacts.playerFacts(126))
  }

  @Test def firstServeFacts_HARD_2010 {
    val firstServeFacts = atpApi.firstServeFacts(SurfaceEnum.HARD, 2010)
    assertEquals(63, firstServeFacts.playerFacts.size)
    assertEquals(60.238, firstServeFacts.firstServeAvgPct(), 0.001)

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

  @Test def pointWonFacts_FIRST_SERVE_POINTS_WON_CLAY_2010 {
    val pointWonFacts = atpApi.pointWonFacts(PointWonFactEnum.FIRST_SERVE_POINTS_WON, SurfaceEnum.CLAY, 2010)
    assertEquals(71, pointWonFacts.playerFacts.size)
    assertEquals(68.971, pointWonFacts.pointWonAvgPct(), 0.001)

    assertEquals(PointWonFact(1, "Tomas Berdych", 569, 718, 79, 18), pointWonFacts.playerFacts(0))
    assertEquals(PointWonFact(2, "Sam Querrey", 631, 817, 77, 19), pointWonFacts.playerFacts(1))
    assertEquals(PointWonFact(71, "Oscar Hernandez", 327, 543, 60, 11), pointWonFacts.playerFacts(70))
  }

  @Test def pointWonFacts_FIRST_SERVE_POINTS_WON_GRASS_2010 {
    val pointWonFacts = atpApi.pointWonFacts(PointWonFactEnum.FIRST_SERVE_POINTS_WON, SurfaceEnum.GRASS, 2010)
    assertEquals(127, pointWonFacts.playerFacts.size)
    assertEquals(73.047, pointWonFacts.pointWonAvgPct(), 0.001)

    assertEquals(PointWonFact(1, "Mardy Fish", 442, 517, 85, 13), pointWonFacts.playerFacts(0))
    assertEquals(PointWonFact(2, "Robin Soderling", 261, 308, 85, 5), pointWonFacts.playerFacts(1))
    assertEquals(PointWonFact(127, "Ergun Zorlu", 0, 0, 0, 2), pointWonFacts.playerFacts(126))
  }

  @Test def pointWonFacts_FIRST_SERVE_POINTS_WON_HARD_2010 {
    val pointWonFacts = atpApi.pointWonFacts(PointWonFactEnum.FIRST_SERVE_POINTS_WON, SurfaceEnum.HARD, 2010)
    assertEquals(63, pointWonFacts.playerFacts.size)
    assertEquals(73.476, pointWonFacts.pointWonAvgPct(), 0.001)

    assertEquals(PointWonFact(1, "Ivo Karlovic", 892, 1057, 84, 22), pointWonFacts.playerFacts(0))
    assertEquals(PointWonFact(2, "Andy Roddick", 2243, 2818, 80, 57), pointWonFacts.playerFacts(1))
    assertEquals(PointWonFact(63, "Michael Russell", 811, 1234, 66, 25), pointWonFacts.playerFacts(62))
  }

  @Test def pointWonFacts_FIRST_SERVE_POINTS_WON_CLAY_2009 {
    val pointWonFacts = atpApi.pointWonFacts(PointWonFactEnum.FIRST_SERVE_POINTS_WON, SurfaceEnum.CLAY, 2009)
    assertEquals(78, pointWonFacts.playerFacts.size)
    assertEquals(69.282, pointWonFacts.pointWonAvgPct(), 0.001)

    assertEquals(PointWonFact(1, "Ivo Karlovic", 428, 524, 82, 12), pointWonFacts.playerFacts(0))
    assertEquals(PointWonFact(2, "Roger Federer", 710, 896, 79, 20), pointWonFacts.playerFacts(1))
    assertEquals(PointWonFact(78, "Diego Junqueira", 408, 703, 58, 14), pointWonFacts.playerFacts(77))
  }

  @Test def pointWonFacts_SECOND_SERVE_POINTS_WON_CLAY_2010 {
    val pointWonFacts = atpApi.pointWonFacts(PointWonFactEnum.SECOND_SERVE_POINTS_WON, SurfaceEnum.CLAY, 2010)
    assertEquals(71, pointWonFacts.playerFacts.size)
    assertEquals(50.802, pointWonFacts.pointWonAvgPct(), 0.001)

    assertEquals(PointWonFact(1, "Juan Carlos Ferrero", 363, 615, 59, 35), pointWonFacts.playerFacts(0))
    assertEquals(PointWonFact(2, "Rafael Nadal", 231, 398, 58, 22), pointWonFacts.playerFacts(1))
    assertEquals(PointWonFact(71, "Fabio Fognini", 283, 673, 42, 23), pointWonFacts.playerFacts(70))
  }

  @Test def pointWonFacts_FIRST_SERVE_RETURN_POINTS_WON_CLAY_2010 {
    val pointWonFacts = atpApi.pointWonFacts(PointWonFactEnum.FIRST_SERVE_RETURN_POINTS_WON, SurfaceEnum.CLAY, 2010)
    assertEquals(71, pointWonFacts.playerFacts.size)
    assertEquals(32.366, pointWonFacts.pointWonAvgPct(), 0.001)

    assertEquals(PointWonFact(1, "Juan Ignacio Chela", 571, 1474, 39, 33), pointWonFacts.playerFacts(0))
    assertEquals(PointWonFact(2, "Rafael Nadal", 374, 982, 38, 22), pointWonFacts.playerFacts(1))
    assertEquals(PointWonFact(71, "John Isner", 181, 798, 23, 18), pointWonFacts.playerFacts(70))
  }

  @Test def pointWonFacts_SECOND_SERVE_RETURN_POINTS_WON_CLAY_2010 {
    val pointWonFacts = atpApi.pointWonFacts(PointWonFactEnum.SECOND_SERVE_RETURN_POINTS_WON, SurfaceEnum.CLAY, 2010)
    assertEquals(71, pointWonFacts.playerFacts.size)
    assertEquals(49.732, pointWonFacts.pointWonAvgPct(), 0.001)

    assertEquals(PointWonFact(1, "Rafael Nadal", 370, 616, 60, 22), pointWonFacts.playerFacts(0))
    assertEquals(PointWonFact(2, "Fernando Verdasco", 481, 848, 57, 29), pointWonFacts.playerFacts(1))
    assertEquals(PointWonFact(71, "Peter Luczak", 164, 400, 41, 16), pointWonFacts.playerFacts(70))
  }

  /**Tests for playerFacts.*/
  @Test def playeFacts_surface {

    assertEquals(PlayerFacts(63, 75, 57, 35, 50, 175, 175), atpApi.playerFacts("Roger Federer", SurfaceEnum.CLAY, 2010))
    assertEquals(PlayerFacts(64, 78, 57, 28, 54, 146, 144), atpApi.playerFacts("Roger Federer", SurfaceEnum.GRASS, 2010))
    assertEquals(PlayerFacts(61, 79, 56, 35, 50, 659, 632), atpApi.playerFacts("Roger Federer", SurfaceEnum.HARD, 2010))
  }

  @Test def playeFacts_year {
    assertEquals(PlayerFacts(63, 75, 57, 35, 50, 175, 175), atpApi.playerFacts("Roger Federer", SurfaceEnum.CLAY, 2010))
    assertEquals(PlayerFacts(60, 79, 57, 33, 50, 251, 244), atpApi.playerFacts("Roger Federer", SurfaceEnum.CLAY, 2009))
  }

  @Test def playeFacts_diffrent_players {
    assertEquals(PlayerFacts(74, 68, 59, 36, 54, 397, 398), atpApi.playerFacts("Juan Carlos Ferrero", SurfaceEnum.CLAY, 2010))
    assertEquals(PlayerFacts(62, 75, 47, 30, 49, 153, 156), atpApi.playerFacts("Milos Raonic", SurfaceEnum.CLAY, 2011))
    assertEquals(PlayerFacts(0, 0, 0, 0, 0, 0, 0), atpApi.playerFacts("Milos Raonic", SurfaceEnum.CLAY, 2010))
  }

  @Test(expected = classOf[IllegalArgumentException]) def playeFacts_no_data_available {
    assertEquals(PlayerFacts(0, 0, 0, 0, 0, 0, 0), atpApi.playerFacts("Milos Raonic", SurfaceEnum.CLAY, 2009))
  }

  //Use http://www.atpworldtour.com/Tennis/Players/Fa/A/Michael-Russell.aspx instead of http://www.atpworldtour.com/Tennis/Players/Top-Players/Michael-Russell.aspx
  @Test def playerFacts_otherUrl {
    assertEquals(PlayerFacts(67, 57, 37, 23, 42, 34, 35), atpApi.playerFacts("Michael Russell", SurfaceEnum.CLAY, 2011))

    assertEquals(PlayerFacts(54, 60, 45, 40, 61, 61, 60), atpApi.playerFacts("Cedrik-Marcel Stebe", SurfaceEnum.CLAY, 2011))
    assertEquals(PlayerFacts(54, 60, 45, 40, 61, 61, 60), atpApi.playerFacts("Cedrik Marcel Stebe", SurfaceEnum.CLAY, 2011))

    assertEquals(PlayerFacts(65, 66, 49, 34, 54, 80, 81), atpApi.playerFacts("Edouard Roger-Vasselin", SurfaceEnum.CLAY, 2011))

  }
}