/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.model.semantic

/**
 * Command expressions, e.g. raw strings or integers
 */
abstract case class CommandExpression() extends ExpressionTree

object ColumnExpression {
  implicit def columnAlias2ColumnExpression(column: AliasedColumn) = ColumnExpression(column)
}

case class ColumnExpression(column: AliasedColumn) extends CommandExpression

object ConstantExpression {
  implicit def string2ConstantExpression(constant: String) = new ConstantExpression(constant)
}

case class ConstantExpression(constant: String) extends CommandExpression

/**
 * Conditional (boolean) expressions
 */
abstract case class BooleanExpression() extends ExpressionTree

case class And(condition1: BooleanExpression, condition2: BooleanExpression, conditions: BooleanExpression*) extends BooleanExpression {
  children += condition1 += condition2 ++= conditions
}

case class Or(condition1: BooleanExpression, condition2: BooleanExpression, conditions: BooleanExpression*) extends BooleanExpression {
  children += condition1 += condition2 ++= conditions
}

abstract case class BinaryExpression(left: ExpressionTree, right: ExpressionTree)
  extends BooleanExpression {
  children += left += right
}

case class Equal(override val left: ExpressionTree, override val right: ExpressionTree)
  extends BinaryExpression(left, right)

case class !=(override val left: ExpressionTree, override val right: ExpressionTree)
  extends BinaryExpression(left, right)
case class >(override val left: ExpressionTree, override val right: ExpressionTree)
  extends BinaryExpression(left, right)

case class >=(override val left: ExpressionTree, override val right: ExpressionTree)
  extends BinaryExpression(left, right)

case class <(override val left: ExpressionTree, override val right: ExpressionTree)
  extends BinaryExpression(left, right)

case class <=(override val left: ExpressionTree, override val right: ExpressionTree)
  extends BinaryExpression(left, right)

case class Like(override val left: ExpressionTree, override val right: ExpressionTree)
  extends BinaryExpression(left, right)

case class NotLike(override val left: ExpressionTree, override val right: ExpressionTree)
  extends BinaryExpression(left, right)

case class Case(when: Iterable[(BooleanExpression, CommandExpression)],
                elseExp: CommandExpression = null) extends CommandExpression

import ConstantExpression.string2ConstantExpression
case class Null(override val left: ExpressionTree) extends BinaryExpression(left, "NULL")
case class NotNullComparison(override val left: ExpressionTree) extends BinaryExpression(left, "NOT NULL")
