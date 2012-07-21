/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.model.semantic

import java.lang.String

object SqlConvertible {
  implicit def stringToSql(from: () => String): SqlConvertible = {
    new SqlConvertible {
      def toSql = from()
    }
  }

  implicit def stringToSql(from: String): SqlConvertible = {
    new SqlConvertible {
      def toSql = from
    }
  }
}

trait SqlConvertible{
  def toSql: String
}