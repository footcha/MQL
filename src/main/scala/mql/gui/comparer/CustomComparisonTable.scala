/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.gui.comparer

import javax.swing.{JTable, JLabel}
import mql.comparer.{Empty, XlsRow, ComparedItem}
import javax.swing.table.TableCellRenderer

class CustomComparisonTable(override val title: String) extends CollectionComparisonTable(title) {
  table.peer.setDefaultRenderer(classOf[XlsRow], new TableCellRenderer {
    def getTableCellRendererComponent(table: JTable, value: Any, isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int) = {
      val txt = value match {
        case v: XlsRow => v.key
      }
      new JLabel(txt) {
        setBackground(if (isSelected) table.getSelectionBackground else table.getBackground)
        setOpaque(true)
      }
    }
  })

  def items = model.underlying

  def items_=(items: Seq[ComparedItem]) {
    model = new Model(items) {
      type ModelItem = ComparedItem

      def left = (item: ComparedItem) => item.left match {
        case it: Empty => item.right
        case it => it
      }

      def middle = (item: ComparedItem) => item.middle
    }
  }
}
