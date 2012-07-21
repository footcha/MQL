/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
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