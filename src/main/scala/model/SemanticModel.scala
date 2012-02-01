package mql.model

// Pseudo code
class SemanticModel 
{
  System: Parameter = {
    Language = {
      Version: Version = new Version(1, 0, 0, 1)
    } // END Language
	Checksum: Checksum = "1a5bc1e5a238d3ca06a6262baf51c24e"
  } // END System 
  Conventions: Conventions
  Description
  Historization = {
    Type = // Historization type, i.e. SCD1
	Keys = // List of keys
  }
  Transformation: Transformation = {
    Pattern = // Pattern type
    Definition = // Extended SQL comes here
  }
}

trait Label{
  Name: String
  Metadata: Dictionary[object, object]
}
trait Parameter {
  def Name: Label
  def Value: Label
}
class System extends Parameter {
  def Language: Parameter
}
class Checksum extends Parameter
class Conventions extends Parameter
class Description extends Parameter
class Historization extends Parameter
class Transformation extends Parameter