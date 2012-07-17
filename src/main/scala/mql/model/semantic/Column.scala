package mql.model.semantic

object Column {
  var name = (table: Table, name: String) => {
    val tableLocal = table
    new Column(name) {
      this.table = tableLocal
    }
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

class Column(name: String) extends Name {
  super.name = name
  var table: Table = null
}