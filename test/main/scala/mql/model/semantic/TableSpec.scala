/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.model.semantic

import mql.EqualitySpec
import TableSpec._

object TableSpec {
  val tab1 = Table("T1")
  val tab2 = Table("T2")
}

class TableSpec extends EqualitySpec {
  val tab12 = Table("T1")
  basicSymmetry((tab1, tab12), tab2)
}
