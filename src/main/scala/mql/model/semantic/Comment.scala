package mql.model.semantic

import mql.Checker.withNotNull

trait Comment {
  private[this] var _comment = () => ""

  def comment = _comment()

  def comment_=(comment: => String) {
    withNotNull(comment) {
      _comment = () => comment
    }
  }
}