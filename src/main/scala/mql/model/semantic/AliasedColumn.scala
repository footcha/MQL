package mql.model.semantic

object AliasedColumnCompanion {
  implicit def tupleToAlias(tuple: (Alias[Column], Alias[Table])) = {
    val (column, table) = tuple
    AliasedColumn(table, column)
  }

  implicit def columnToAlias(column: Column) = AliasedColumn(Alias(column.table), Alias(column))

  implicit def aliasToSql(column: AliasedColumn) = {
    new SqlConvertible {
      def toSql = column.table.alias + "." + column.column.alias
    }
  }
}

case class AliasedColumn(table: Alias[Table], column: Alias[Column]) {
  if (table.entity != column.entity.table) sys.error("Table alias and column joinedTable are not same.")

  def x: SqlConvertible = this.asInstanceOf[SqlConvertible]

  import AliasedColumnCompanion.aliasToSql

  override def toString = this.toSql
}