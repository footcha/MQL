package mql.model.semantic

import org.scalatest.{FunSuite}

class SemanticModelTest extends FunSuite {

  test("Label equality") {
    assert(Label == Label)

    val la1 = Label("a")
    val la2 = Label("a")
    val lb1 = Label("b")

    assert(la1 == la2)
    assert(la1 != lb1)
  }

  test("Read-only properties for Label") {
    val l = Label
    shouldBeReadOnlyProperty("name"){
      l.name = ""
    }
  }

  test("Parameter equality"){
//    assert(Parameter === Parameter)
//    assert(Parameter == Parameter)
//    val p1 = Parameter()
//    val p2 = Parameter()
//
//    assert(Parameter() == Parameter())
  }

  test("Read-only properties for Parameter") {
//    val p = Parameter
//    shouldBeReadOnlyProperty("name"){
//      p.name = Label
//    }
//    shouldBeReadOnlyProperty("value"){
//      p.value = Label
//    }
  }

  def shouldBeReadOnlyProperty(propertyName: String)(action: => Unit){
    try {
      action
      fail(String.format("Property '%s' was expected to be readonly.", propertyName))
    } catch{
       case ex: ReadOnlyPropertyException =>
    }
  }
}