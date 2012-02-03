package mql.model.semantic

import collection.mutable.ArraySeq
import collection.LinearSeq

object Label extends Label {
  override def name_=(x: String) {
    throw new ReadOnlyPropertyException("name")
  }

  def apply(): Label = apply(Constants.Undefined)

  def apply(name1: String) = {
    new Label {
      this.name = name1

      override def equals(obj: Any) = {
        val that = obj.asInstanceOf[Label]
        that != null && that.name == name
      }

      override def toString() = String.format("Label: %s", name)
    }
  }
}

trait Label {
  private[this] var _name = Constants.Undefined

  def name: String = _name

  def name_=(x: String) {
    require(x != null && x.length() > 0, "Name length must be greater than 0")
    _name = x
  }
}

object Value {
  //  override def name_=(x: String) {
  //    throw new ReadOnlyPropertyException("value")
  //  }

  def apply[T <: AnyRef](value1: T) = {
    new Value[T] {
      this.value = value1

      //      override def equals(obj: Any) = {
      //        val that = obj.asInstanceOf[Value]
      //        that != null && that.value == value
      //      }

      override def toString() = String.format("Value: %s", value)

      var _value = value1
    }
  }
}


trait Value[T] {
  protected var _value: T

  def value: T = _value

  def value_=(value: T) {
    require(value != null)
    _value = value

  }
}

trait StringValue extends Value[String]

trait Metadata {
  def metadata: LinearSeq[Value[String]]
}