package dk.atp.api.domain

object SurfaceEnum extends Enumeration {
    type SurfaceEnum = Value
    val CLAY = Value(1)
    val GRASS = Value(2)
    val HARD = Value(3)
    
    def fromText(surface:String) = surface match {
      case "CLAY" => CLAY
      case "GRASS" => GRASS
      case "HARD" => HARD
    }
    
    override def toString() = SurfaceEnum.values.mkString("SurfaceEnum [", ", ", "]")
  }