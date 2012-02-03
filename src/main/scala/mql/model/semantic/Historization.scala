package mql.model.semantic

import collection.LinearSeq

class Historization /*extends Parameter with Parameters*/ {

  case class Increment()

  case class Incremental() extends Increment

  final case class Undefined() extends Increment

  case class Full() extends Increment

  case class Type(version: Double, increment: Increment) extends Value[Type] {
    protected var _value = this
  }

  case class SCD0I() extends Type(0, Incremental())

  case class SCD0F() extends Type(0, Full())

  case class SCD1I() extends Type(1, Incremental())

  case class SCD1F() extends Type(1, Full())

  case class SCD2I() extends Type(2, Incremental())

  case class SCD2F() extends Type(2, Full())

  case class SCD25I() extends Type(2.5, Incremental())

  case class SCD25F() extends Type(2.5, Full())

  object Type {
    val SCD0I = new SCD0I()
    val SCD0F = new SCD0F()
    val SCD1I = new SCD1I()
    val SCD1F = new SCD1F()
    val SCD2I = new SCD2I()
    val SCD2F = new SCD2F()
    val SCD25I = new SCD25I()
    val SCD25F = new SCD25F()
    val undefined = new Type(-1, Undefined())

    //def apply(version: Double, increment: Increment) = new Type(version, increment)
  }

  def $type(): Parameter[Label with Metadata, Type with Metadata] =
    new Parameter[Label with Metadata, Type with Metadata] {
      override def label = null

      override def value = null
    }

  $type().value.increment
  $type().value match {
    case SCD0F() => 1
  }
  $type.value.metadata(0).value
  $type.label.name = ""
  $type.label.metadata(0).value

  abstract class Key extends StringValue with Metadata
  // List of keys
  abstract class Keys extends StringValue with LinearSeq[Key]

  def keys: Parameter[Label with Metadata, Keys] =
    new Parameter[Label with Metadata, Keys] {
      override def label = null

      override def value = null
    }
  keys.label.name = ""
  keys.value(0).value = ""
  keys.value(0).metadata(0).value = ""
}