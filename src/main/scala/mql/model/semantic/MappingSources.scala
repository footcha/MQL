/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.model.semantic

import collection.mutable.ListBuffer
import scala.collection.mutable.{HashMap, Buffer}

object Relation {
  implicit def relationToSqlConvertible(relation: Relation): SqlConvertible = {
    relation match {
      case rel: SqlConvertible => rel
      case r => r.toString
    }
  }
}

object JoinType {
  val left = LeftJoin()
  val right = RightJoin()
  val inner = InnerJoin()
}
abstract case class JoinType() extends SqlConvertible
case class LeftJoin() extends JoinType {
  def toSql: String = "LEFT JOIN"
}
case class RightJoin() extends JoinType{
  def toSql: String = "RIGHT JOIN"
}
case class InnerJoin() extends JoinType{
  def toSql: String = "INNER JOIN"
}

trait Relation extends SqlExpression {
  def joinType: JoinType = JoinType.left
  def joinedTable: Alias[Table]
}

trait Relations extends Iterable[Relation] {
  def sources: MappingSources
  private val underlying = new ListBuffer[Relation]
  def +=(sql: Relation) {
    underlying += sql
  }

  def iterator = underlying.iterator
}

trait MappingSources { thisMappingSources =>
  private val empty = ""
  private val emptySql: SqlConvertible = empty

  def mapping: Mapping
  private val _relations: Relations = new Relations {
    def sources = thisMappingSources
  }

  def relations: Relations = _relations

  var groupBy: SqlConvertible = emptySql

  // TODO change it to property
  var activeFlagManipulation = empty

  private var _mainSource2 = new ListBuffer[Table]
  def mainSource: Buffer[Table] = _mainSource2

  // TODO change it to property
  var filter = new HashMap[Any,  ListBuffer[SqlConvertible]] with SqlConvertible {
    override def apply(key: Any) = {
      getOrElseUpdate(key, new ListBuffer[SqlConvertible])
    }

    def toSql = {
      this.values map(_.map(_.toSql) mkString " OR ") mkString " AND "
    }
  }

  private var _matchingRules = new ListBuffer[Column]
  def matchingRules: Buffer[Column] = _matchingRules
}
