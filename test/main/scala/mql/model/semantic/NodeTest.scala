/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.model.semantic

import org.scalatest.FunSuite
import ColumnNode._
import AliasedColumnCompanion.columnToAlias

class NodeTest extends FunSuite {
  implicit def intToExpression(obj: Int) = StringNode(obj.toString)

  val table = new Table("TestTable")
  val col1: ColumnAlias = Column(table, "TestColumn1")
  val col2: ColumnAlias = Column(table, "TestColumn2")
  val col3: ColumnAlias = Column(table, "TestColumn3")
  // Example:
  // (col1 = col2 AND col2 = 2) OR col2 > col1
  val complexCond = Or(And(Equal(col1, col2), Equal(col2, 2)), >(col2, col1))

  val constant = StringNode("constant")
  val colExp1 = ColumnNode(col1)
  val colExp2 = ColumnNode(col2)
  val colExp3 = ColumnNode(col3)

  import org.scalatest.matchers.ShouldMatchers._
  test("Parents is set") {
    def testChildren(parent: Node) {
      for (val child <- parent.children) {
        child.parent should be === parent
        testChildren(child)
      }
    }

    complexCond.parent.parent.parent.parent
    testChildren(complexCond)
  }

  for (val params <- List(
    constant -> 0,
    colExp1 -> 0,
    Equal(colExp1, colExp2) -> 2
  )) {
    val (testedObject, expectedLength) = params
    test("Length test: " + testedObject.getClass.getName) {
      testedObject.children should have length expectedLength
    }
  }

  test("Concatenate children") {
    import org.scalatest.matchers.ShouldMatchers._
    implicit val sep = SeparatorNode("X")
    val con = ConcatenationNode(colExp1, colExp2)

    val expected = List(colExp1, sep, colExp2)
    con.children should equal (expected)
  }
}