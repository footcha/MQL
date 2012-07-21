/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql

object Checker {
  def withNotNull[T](obj: T)(function: => Unit) {
    assert(obj != null)
    function
  }

  def ensureNotNull[T](obj: T): T = {
    assert(obj != null)
    obj
  }
}