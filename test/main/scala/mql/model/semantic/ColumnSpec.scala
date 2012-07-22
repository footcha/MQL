/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.model.semantic

import org.scalatest.FlatSpec
import mql.EqualitySpec
import org.scalatest.matchers.ShouldMatchers._
import TableSpec._
import ColumnSpec._

object ColumnSpec {
  val col1Tab1 = new Column(tab1, "C1")
  val col1Tab2 = new Column(tab1, "C2")
  val col2Tab1 = new Column(tab2, "C1")
}

class ColumnSpec extends FlatSpec with EqualitySpec {
  val col12Tab1 = new Column(tab1, "C1")
  val col13Tab1 = new Column(tab1, "C1")
  val col21tab1 = new Column(tab1, "C2")

  "Equal columns (in different tables)" should "not be equal" in {
    col1Tab1 should not equal col2Tab1
  }

  basicSymmetry((col1Tab1, col12Tab1), col21tab1)

  "Equal columns" should behave like transitiveObjects(col1Tab1, col12Tab1, col13Tab1)
}