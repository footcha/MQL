/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.transformation

import mql.model.semantic._
import mql.model.semantic.BooleanExpression
import mql.model.semantic.ExpressionTree

class Expression2Sql(private val expression: BooleanExpression) extends SqlConvertible {
  def toSql: String = traverse(expression)

  private def traverse(expression: ExpressionTree): String = {
    import AliasedColumnCompanion.aliasToSql
    expression match {
      case e: BinaryExpression => process(e)
      case e: BooleanExpression => process(e)
      case ColumnExpression(column) => column.toSql
      case e: ConstantExpression => process(e)
      case t => sys.error("should not get here: " + t)
    }
  }

  protected def process(expression: ConstantExpression) =  "'" + expression.constant + "'"

  protected def process(expression: BinaryExpression): String = {
    val operator = expression match {
      case e: Equal => "="
    }
    val BinaryExpression(left, right) = expression
    traverse(left) + " " + operator + " " + traverse(right)
  }

  protected def process(expression: BooleanExpression): String = {
    expression match {
      case And(left, right) => "(" + traverse(left) + " AND " + traverse(right) + ")"
      case Or(left, right) => "(" + traverse(left) + " OR " + traverse(right) + ")"
    }
  }
}