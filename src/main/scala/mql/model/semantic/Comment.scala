/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
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