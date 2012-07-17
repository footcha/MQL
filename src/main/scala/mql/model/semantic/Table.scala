package mql.model.semantic

import collection.mutable.{Buffer, ListBuffer}

object TableColumns {
  implicit def byName(columns: Buffer[Column]) = {
    new {
      def byName(name: String): Column = {
        columns.find(c => c.name == name).head
      }
    }
  }
}

class Table(override val name: String)
  extends Metadata
  with Name
  with Comment
{ thisTable =>
  private var columnsF: Buffer[Column] = new ListBuffer[Column]()
  def columns = columnsF
  protected def columns_=(columns: Buffer[Column]) {
    columnsF = columns
  }
  val primaryKey: Buffer[Column] = new ListBuffer[Column]

  protected def createColumn(name: String) = new Column(name) {
    table = thisTable
  }
}