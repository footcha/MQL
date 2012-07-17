package mql.model.semantic

import mql.StringFormatter.RichFormatter

object AliasCompanion {
  implicit def entityToAlias[T <: Name](entity: T) = {
    Alias(entity)
  }

  def byName[T <: Name]: T => String = entity => entity.name
}

case class Alias[+T <: Name](entity: T, aliasTemplate: Name => String = AliasCompanion.byName)
  extends SqlConvertible {

  val alias = aliasTemplate(entity)

  def toSql =
    if (entity.name != alias)
      "#{object} AS #{alias}" richFormat Map(
        "object" -> entity.name,
        "alias" -> alias
      )
    else entity.name

  override def toString = toSql
}