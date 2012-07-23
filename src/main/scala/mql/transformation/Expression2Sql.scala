/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.transformation

import mql.model.semantic._
import mql.model.semantic.BooleanNode
import mql.model.semantic.Node

class Expression2Sql(private val expression: BooleanNode) extends SqlConvertible {
  def toSql: String = traverse(expression)

  private def traverse(expression: Node): String = {
    import AliasedColumnCompanion.aliasToSql
    expression match {
      case e: BinaryNode => process(e)
      case e: BooleanNode => process(e)
      case ColumnNode(column) => column.toSql
      case e: ConstantNode => process(e)
      case t => sys.error("should not get here: " + t)
    }
  }

  protected def process(expression: ConstantNode): String = expression match {
    case StringNode(s) => "'" + s + "'"
  }

  protected def process(expression: BinaryNode): String = {
    val operator = expression match {
      case e: Equal => "="
      case e: NotLike => "NOT LIKE"
      case e => e.getClass.getName.toUpperCase
    }
    val BinaryNode(left, right) = expression
    traverse(left) + " " + operator + " " + traverse(right)
  }

  protected def process(expression: BooleanNode): String = {
    import mql.StringFormatter.RichFormatter
    val (left, operator, right) = expression match {
      case And(l, r) => (l, "AND", r)
      case Or(l, r)  => (l, "OR",  r)
    }
    "({left} {operator} {right})" richFormat (
      'left -> traverse(left),
      'operator -> operator,
      'right -> traverse(right)
      )
  }
}