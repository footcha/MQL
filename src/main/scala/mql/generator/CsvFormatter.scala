/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.generator

import collection.mutable.{HashMap, ArrayBuffer}
import java.io.Writer

class CsvFormatter(private val writer: Writer) {
  var fieldSeparator = ";"
  var rowSeparator = "\r\n"

  class Head {
    private val labels = new ArrayBuffer[String]()
    var isClosed = false
    var visible = true
    val fields = new Iterable[String] {
      def +=(name: String) {
        if (isClosed) throw new Exception("Head is closed for modifications.")
        if (name.length() <= 30) {
          labels += name
          return
        }
        val errorMsg = String.format("Column name cannot be longer than 30 characters. Column name [{%s}] contains %s characters instead.",
          name, name.length().toString
        )
        throw new Exception(errorMsg)
      }

      def iterator = labels.iterator
    }

    def close() {
      isClosed = true
      body.isClosed = false

      val rowBufferTmp = HashMap[String, String]()
      val iteration =
        if (visible) {
          (label: String) => {
            writeField(label)
            rowBufferTmp(label) = null
          }
        } else {
          (label: String) => rowBufferTmp(label) = null
        }
      for (label <- labels) iteration(label)
      if (visible) writeLine()
    }
  }

  val head = new Head

  class Body {
    private[CsvFormatter] var isClosed = true

    private[CsvFormatter] val buffer = HashMap[String, String]()

    val field = new {
      def update(label: String, value: String) {
        if (isClosed) throw new Exception("Body is closed for modifications.")
        buffer(label) = value
      }
    }

    def flush() {
      newRow()
    }
  }

  val body = new Body

  private def writeField(value: String) {
    write("\"" + value + "\"")
    write(fieldSeparator)
  }

  private def writeLine() {
    write(rowSeparator)
  }

  private def write(value: String) {
    writer.write(value)
  }

  private def newRow() {
    for (label <- head.fields) {
      val value = body.buffer.getOrElse(label, "")
      writeField(value)
    }
    writeLine()
  }
}