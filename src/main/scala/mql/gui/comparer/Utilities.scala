/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.gui.comparer

object Utilities {
  def resource(name: String) = ClassLoader.getSystemClassLoader.getResource(name)
}