package mql.model.semantic

import mql.Repository

trait TableRepository[T<:Table] extends Repository[T] {
  def findByName(tableName: String): T =
    repository.find(table => table.name == tableName) match {
      case Some(t) => t
      case None => onNotExists(tableName)
    }

  protected val defaultNotExists = (tableName: String) => sys.error("Table [" + tableName + "] was not found")
  var onNotExists: String => T = defaultNotExists
}