/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.model.semantic

object AliasedColumnCompanion {
  def alias(table: Alias[Table], column: Alias[Column]) = ColumnAlias(table, column)

  import Alias.byName
  implicit def columnToAlias(column: Column) = ColumnAlias(Alias(column.table), Alias(column))

  implicit def aliasToSql(column: ColumnAlias) = {
    new SqlConvertible {
      def toSql = column.table.alias + "." + column.column.alias
    }
  }
}

case class ColumnAlias(table: Alias[Table], column: Alias[Column]) {
  if (table.entity != column.entity.table) sys.error("Table alias and column joinedTable are not same.")

  def x: SqlConvertible = this.asInstanceOf[SqlConvertible]

  import AliasedColumnCompanion.aliasToSql

  override def toString = this.toSql
}