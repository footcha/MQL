/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.model.semantic

trait SqlExpression extends SqlConvertible

object RawExpression {
  implicit def stringToSqlExpression(sql: String): RawExpression = {
    new RawExpression(sql)
  }
  implicit def columnAliasToSqlExpression(column: AliasedColumn): Expression = {
    null
  }
}

import RawExpression._

case class RawExpression(sql: String) extends Expression

abstract case class Expression() {
  private[this] var _parent: Expression = _
  def parent: Expression = _parent
  def parent_=(parent: Expression) { _parent = parent}
  def children: Iterable[Expression] = null // TODO automatic bi-directional iterable
}

object Operator {
  val Is = Operator()
  val IsNot = Operator()
  val `=` = Operator()
  val != = Operator()
  val <> = !=
  val > = Operator()
  val >= = Operator()
  val < = Operator()
  val <= = Operator()
  val Like = Operator()
  val NotLike = Operator()
}

object Test {
  implicit def intToExpression(obj: Int): Expression = {
    new Expression {}
  }

  val col1: AliasedColumn = null //new Column("1")
  val col2: AliasedColumn = null //new Column("2")
  val col3: AliasedColumn = null//new Column("3")
  // Example:
  // (col1 = col2 AND col2 = 2) OR col2 > col1
  Or(And(BinaryComparison(Operator.`=`, col1, col2), BinaryComparison(Operator.`=`, col2, 2)), BinaryComparison(Operator.>, col2, col1))
}

import Operator._

case class Condition()
case class And(condition1: Condition, condition2: Condition, conditions: Condition*) extends Condition
case class Or(condition1: Condition, condition2: Condition, condition: Condition*) extends Condition
case class SimpleCondition(expression: BooleanExpression) extends Condition

case class Operator()

abstract case class BooleanExpression() extends Expression

case class BinaryComparison(operator: Operator, left: Expression, right: Expression) extends Condition

case class UnaryComparison(operator: Operator, left: Expression, right: Expression) extends BooleanExpression

case class NullComparison(column: AliasedColumn)
  extends BinaryComparison(Is, column, "NULL")

case class NotNullComparison(column: AliasedColumn)
  extends BinaryComparison(IsNot, column, "NULL")

case class And2(left: BooleanExpression, right: BooleanExpression)