package dk.atp.api.domain

case class PlayerFacts(
  aces: Int,
  doubleFaults: Int,
  firstServeHits: Int, firstServeTotal: Int,
  firstServeWon: Int, firstServePlayed: Int,
  secondServeWon: Int, secondServePlayed: Int,
  breakPointsSaved: Int, breakPointsTotal: Int,
  serviceGames: Int,
  servicePointsWon: Int, servicePointsTotal: Int)