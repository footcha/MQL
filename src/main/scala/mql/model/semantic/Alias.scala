/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.model.semantic

import mql.StringFormatter.RichFormatter

object Alias {
  implicit def entityToAlias[T <: Name](entity: T) =  Alias(entity)

  implicit def byName: Name => String = entity => entity.name

  val extract = new {
    def unapply(alias: Alias[_ <: Name]): Option[(_ <: Name, String)] = {
      Some(alias.entity, alias.alias)
    }
  }
}

case class Alias[+T <: Name](entity: T)(implicit aliasTemplate: Name => String)
  extends SqlConvertible {

  val alias = aliasTemplate(entity)

  def toSql =
    if (entity.name != alias)
      "{object} AS {alias}" richFormat (
        'object -> entity.name,
        'alias -> alias
        )
    else entity.name

  override def toString = toSql
}