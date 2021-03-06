/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
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
      case Some(col) => sys.error("Mapping for column [" + col.column.toSql + "] is already defined.")
    }
  }

  def iterator = columns.values.iterator

  def mapping: Mapping
}