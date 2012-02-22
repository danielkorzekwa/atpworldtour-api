package dk.atp.api

import dk.atp.api.tournament.TournamentAtpApi._
import ATPMatchesLoader._
/**Load matches from http://www.atpworldtour.com/ web site.*/

object ATPMatchesLoader {
   case class MatchComposite(tournament: Tournament, tennisMatch: Match, matchFacts: MatchFacts)
}

trait ATPMatchesLoader {

   def loadMatches(year: Int): List[MatchComposite]
  
}