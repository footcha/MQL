package mql.model.semantic

import java.lang.String
import collection.mutable.HashMap

trait ColumnMappings extends Iterable[ColumnMapping] {
  protected var columns = new HashMap[String, ColumnMapping]

  private def find(column: Column): Option[ColumnMapping] = {
    columns.get(column.name)
  }

  def apply(column: Column) = find(column)

  @deprecated
  def apply(columnName: String) = columns.values.find(mp => mp.column.name == columnName).get

  def update(column: Column, value: ColumnMapping) {
    columns(column.name) = value
  }

  def +=(columnMapping: ColumnMapping) {
    find(columnMapping.column) match {
      case None => {
        columnMapping.mapping = mapping
        columns += ((columnMapping.column.name, columnMapping))
      }
      case Some(col) => sys.error("Column [" + col.column.toSql + "] already defined")
    }
  }

  def iterator = columns.values.iterator

  def mapping: Mapping
}