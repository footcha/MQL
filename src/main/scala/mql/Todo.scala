/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql

object Todo {
  def apply(): Nothing = apply("")

  def apply(message: String): Nothing = throw new UnsupportedOperationException("Not implemented: TODO " + message)
}
