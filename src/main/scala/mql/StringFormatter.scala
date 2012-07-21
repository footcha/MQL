/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql

object StringFormatter {
  /**
   * @see http://stackoverflow.com/f/4051500/256008
   */
  implicit def RichFormatter(string: String) = new {
    def richFormat(replacement: Map[String, String]) =
      (string /: replacement) {
        (res, entry) => res.replaceAll("#\\{%s\\}".format(entry._1), entry._2)
      }
  }
}