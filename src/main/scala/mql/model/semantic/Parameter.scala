package mql.model.semantic

//object Parameter extends Parameter {
////  super.name = Label
////  super.value = Value
//
//  override def name_=(name1: Label) = throw new ReadOnlyPropertyException("name")
//
//  override def value_=(value1: Label) = throw new ReadOnlyPropertyException("value")
//
////  def apply() = new Parameter {
////    override def equals(obj: Any) = {
////      val that = obj.asInstanceOf[Parameter]
////      (that != null
////        //&& that.name == name
////        && that.value == value)
////    }
////  }
//}

trait Parameter[L <: Label, V <: Value[_]] {
  def label: L
  def value: V
//  private var _name = Label()
//  private var _value = Label()
//
//  def name: Label = _name
//
//  def name_=(name1: Label) {
//    require(name1 != null)
//    _name = name1
//  }
//
//  def value: Label = _value
//
//  def value_=(value1: Label) {
//    require(value1 != null)
//    _value = value1
//  }
}