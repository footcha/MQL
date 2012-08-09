/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.transformation

import mql.model.semantic._
import edw.StageTable
import mql.model.semantic.Or
import mql.model.semantic.And
import mql.model.semantic.Equal
import mql.model.semantic.NotLike
import mql.Todo
import org.slf4j.LoggerFactory

object Expression2Sql {
  private val logger = LoggerFactory.getLogger(classOf[Expression2Sql])
}

class Expression2Sql(private val expression: Node) extends SqlConvertible {
  import Expression2Sql.logger
  def toSql: String = processNode(expression)

  protected def processNode(expression: Node): String = {
    import AliasedColumnCompanion.aliasToSql
    expression match {
      case NullNode => "IS NULL"
      case e: BinaryNode => processBinaryNode(e)
      case e: BooleanNode => processBooleanNode(e)
      case ColumnNode(column) => {
        val table = column.table.entity
        if (!table.isInstanceOf[StageTable]) logger.error("Column mapping of target column %s is not implemented." format column)
        column.toSql
      }
      case e: ConstantNode => processConstantNode(e)
      case node => processUnknownNode(node)
    }
  }

  protected def processConstantNode(expression: ConstantNode): String = expression match {
    case StringNode(s) => "'" + s + "'"
  }

  protected def processBinaryNode(expression: BinaryNode): String = {
    val operator = expression match {
      case e: Equal => "="
      case e: NotLike => "NOT LIKE"
      case e => e.getClass.getName.toUpperCase
    }
    val BinaryNode(left, right) = expression
    if (left == NullNode || right == NullNode) Todo("NullNode binary comparison is not implemented yet.")
    processNode(left) + " " + operator + " " + processNode(right)
  }

  protected def processBooleanNode(expression: BooleanNode): String = {
    expression match {
      case True => "1"
      case False => "0"
      case And(l, r) => toSqlString(l, "AND", r)
      case Or(l, r) => toSqlString(l, "OR", r)
      case node => processUnknownNode(node)
    }
  }

  protected def processUnknownNode(node: Node): String = {
    throw new InvalidNodeException(node)
  }

  protected def processChildren(node: Node): String = {
    (for (val child <- node.children) yield {
      processNode(child)
    }).mkString
  }

  protected def toSqlString(left: BooleanNode, operator: String, right: BooleanNode) = {
    import mql.StringFormatter.RichFormatter
    "({left} {operator} {right})" richFormat(
      'left -> processNode(left),
      'operator -> operator,
      'right -> processNode(right)
      )
  }
}