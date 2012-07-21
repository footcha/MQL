/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.model.semantic

object Column {
  @deprecated
  var name = (table: Table, name: String) => apply(table, name)

  def apply(table: Table, columnName: String) = {
    val tableLocal = table
    new Column(columnName) { this.table = tableLocal }
  }

  implicit def columnToSql(column: Column): SqlConvertible = {
    new SqlConvertible {
      def toSql = column.table.name + "." + column.name
    }
  }

  implicit def columnToSql(column: (Column, Alias[Table])): SqlConvertible = {
    val (col, table) = column
    new SqlConvertible {
      def toSql = table.alias + "." + col.name
    }
  }
}

import mql.Checker._
case class Column(override val name: String) extends Name {
  var _table: Table = null // TODO Table is mandatory. Extend ctor with table.
  def table = ensureNotNull(_table)
  def table_=(table: Table) {
    withNotNull(table) { _table = table }
  }
}