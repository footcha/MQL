package mql.transformation

import mql.model.semantic._

object StandardColumnTransformation {
  def oneToOne(from: AliasedColumn, to: Column): ColumnMapping = {
    new ColumnMapping {
      column = to
      mappedTo += from.column.entity
      transformation = NoTransformation(from)
    }
  }

  def translateOverFor(column1: Column)(from: AliasedColumn, to: AliasedColumn) = {
    new ColumnMapping {
      column = column1
      mappedTo += from.column.entity
      mappedTo += to.column.entity
      transformation = TokenizedTransformation("", "from" -> from, "to" -> to)
    }
  }
}