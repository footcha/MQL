/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.transformation

import mql.model.semantic._

object StandardColumnMapping {
  def oneToOne(from: ColumnAlias, to: Column): ColumnMapping = {
    new ColumnMapping {
      column = to
      mappedTo += from.column.entity
      transformation = ColumnNode(from)
    }
  }

  @deprecated
  def translateOverFor(column1: Column)(from: ColumnAlias, to: ColumnAlias) = {
    new ColumnMapping {
      column = column1
      mappedTo += from.column.entity
      mappedTo += to.column.entity
      transformation = TokenizedNode("", 'from -> from, 'to -> to)
    }
  }
}