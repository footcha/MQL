package mql.model.semantic

import mql.Checker.withNotNull

trait Name {
  private[this] var _name = () => ""
  def name: String = _name()
  def name_=(name: => String) {
    withNotNull(name) {
      _name = () => name
    }
  }
}