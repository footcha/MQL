/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.model.semantic

import collection.mutable.ListBuffer

/**
 * Expression nodes, e.g. raw strings or integers
 */
case object EmptyNode extends Node

case object NullNode extends Node

object ColumnNode {
  implicit def columnAlias2ColumnExpression(column: ColumnAlias) = ColumnNode(column)
}

sealed case class ColumnNode(column: ColumnAlias) extends Node

abstract class ConstantNode(val value: Any) extends Node

object StringNode {
  implicit def string2Constant(value: String) = new StringNode(value)
}

sealed case class StringNode(override val value: String) extends ConstantNode

sealed case class IntNode(override val value: Int) extends ConstantNode

object IntNode {
  implicit def int2Constant(value: Int) = new IntNode(value)
}

sealed case class FunctionNode(name: String, parameters: Node*) extends Node

/**
 * Conditional (boolean) expressions
 */
object BooleanNode {
  implicit def boolean2Node(value: Boolean): BooleanNode = value match {
    case true => True
    case false => False
  }
}

case object True extends BooleanNode

case object False extends BooleanNode

abstract class BooleanNode extends Node

sealed case class And(condition1: BooleanNode, condition2: BooleanNode, conditions: BooleanNode*)
  extends BooleanNode {
  children += condition1 += condition2 ++= conditions
}

sealed case class Or(condition1: BooleanNode, condition2: BooleanNode, conditions: BooleanNode*)
  extends BooleanNode {
  children += condition1 += condition2 ++= conditions
}

object BinaryNode {
  def unapply(e: BinaryNode): Option[(Node, Node)] = Some(e.left, e.right)
}

abstract class BinaryNode(val left: Node, val right: Node)
  extends BooleanNode {
  children += left += right
}

sealed case class Equal(override val left: Node, override val right: Node)
  extends BinaryNode(left, right)

sealed case class !=(override val left: Node, override val right: Node)
  extends BinaryNode(left, right)
sealed case class >(override val left: Node, override val right: Node)
  extends BinaryNode(left, right)

sealed case class >=(override val left: Node, override val right: Node)
  extends BinaryNode(left, right)

sealed case class <(override val left: Node, override val right: Node)
  extends BinaryNode(left, right)

sealed case class <=(override val left: Node, override val right: Node)
  extends BinaryNode(left, right)

sealed case class Like(override val left: Node, override val right: Node)
  extends BinaryNode(left, right)

sealed case class NotLike(override val left: Node, override val right: Node)
  extends BinaryNode(left, right)

sealed case class Case(when: Iterable[(BooleanNode, Node)],
                elseExp: Node = null) extends Node

/**
 * Nodes that further extend SQL standard syntax
 */
object SeparatorNode {
  implicit val convert = SeparatorNode("#")
}

sealed case class SeparatorNode(separator: String) extends ConstantNode

sealed case class ConcatenationNode(commands: Node*)(implicit separator: SeparatorNode)
extends Node {
  assert(commands.length >= 2, "Concatenation is possible only for at least 2 commands." + commands.length + " given instead.")
  children ++= {
    val buffer = new ListBuffer[Node]
    buffer ++= commands.toList flatMap (List(_, separator))
    buffer.remove(buffer.length - 1)
    buffer
  }
}

sealed case class TokenizedNode(sql: String, columnsSeq: (Symbol, ColumnAlias)*)
extends Node {
  import ColumnNode.columnAlias2ColumnExpression
  lazy val columnMap = columnsSeq.toMap
  lazy val columns = columnMap.values
  children ++= columns.map(columnAlias2ColumnExpression(_))
}

object TokenizedNode {
  val ! = new {
    def unapply(t: TokenizedNode): Option[(String, Seq[(Symbol, ColumnAlias)])] = Some(t.sql, t.columnMap.toSeq)
  }
}