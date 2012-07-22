/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.model.semantic

import collection.mutable.ListBuffer

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


object Separator {
  implicit val convert = Separator("#")
}

case class Separator(override val constant: String) extends ConstantExpression(constant)

case class Concatenate(commands: CommandExpression*)(implicit separator: Separator)
  extends CommandExpression {
  assert(commands.length >= 2, "Concatenation is possible only for at least 2 commands." + commands.length + " given instead.")
  children ++= {
    val buffer = new ListBuffer[CommandExpression]
    buffer ++= commands.toList flatMap (List(_, separator))
    buffer.remove(buffer.length - 1)
    buffer
  }
}

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

case class BinaryExpression(left: ExpressionTree, right: ExpressionTree)
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
