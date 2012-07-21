/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.generator

import io.BufferedSource
import collection.mutable.HashMap

class CsvReader(reader: BufferedSource) {
  type EventType = HashMap[String, String] => Unit
  private var isHeadIncluded = true
  private var _evt: EventType = line => Unit


  def readEvent_=(evt: EventType) {
    _evt = evt
  }

  def readEvent = _evt

  private def parseLine(line: String): Array[String] = {
    val line2 = line.substring(1, line.length() - 1)
    val array = line2.split("\",\"")
    array
  }

  def read() {
    if (!isHeadIncluded) sys.error("Reading CSV without header is not suported")

    val header = reader.getLines
    val fieldNames = parseLine(header.next())
    val body = new HashMap[String, String]
    for (val item <- fieldNames) {
      body += ((item, ""))
    }

    for (val line <- reader.getLines) {
      var idx = 0
      val array: Array[String] = parseLine(line)
      for (val item <- array) {
        body(fieldNames(idx)) = item
        idx += 1
      }
      readEvent(body)
    }
  }
}