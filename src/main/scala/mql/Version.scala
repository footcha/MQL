/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql

import java.text.SimpleDateFormat

object Version {
  val version = "1.0.55"
  private val releaseDate = "20120807165624"
  val releasedAt = new SimpleDateFormat("yyyyMMddHHmmss").parse(releaseDate)
}