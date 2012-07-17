package mql.model.semantic

class ReadOnlyPropertyException(propertyName: String) extends Exception {
  override def getMessage() = {
    String.format("Property '%s' is read-only", propertyName)
  }
}

object Constants {
  val Undefined = "undefined"
}

class SemanticModel extends Parameters {
  var name = Label()

  var description = new Parameter[Label with Metadata, StringValue with Metadata] {
    def label: Label with Metadata = null

    def value: StringValue with Metadata = null
  }

  description.value.value.contains("value")
  description.value.metadata(0).value = "new metadataForValue value"
  description.label.name = "new name"
  description.label.metadata
  description.label.metadata(0).value

  var transformation = new Transformation

  var historization = new Historization

  var difficulty = new Difficulty
}

class Difficulty

//extends Parameter

class Transformation /*extends Parameter with Parameters*/ {

  // Pattern type
  class Pattern

  //extends Parameter

  // Extended SQL comes here
  class Definition

  def pattern = new Pattern

  def definition = new Definition
}