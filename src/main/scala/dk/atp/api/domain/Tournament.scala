package dk.atp.api.domain

import java.util.Date
import dk.atp.api.domain.SurfaceEnum._

case class Tournament(tournamentTime: Date, tournamentName: String, surface: SurfaceEnum, numOfSet: Int)