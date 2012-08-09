/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.gui.comparer

import swing.{Table, ScrollPane}
import javax.swing.table.{TableCellRenderer, TableModel}
import javax.swing.event.TableModelListener
import java.awt.Color
import mql.comparer.{ComparedItem, ComparisonResult}
import javax.swing.{JTable, SwingConstants, ImageIcon, JLabel}

class CollectionComparisonTable(val title: String) extends ScrollPane {

  object ComparisonRenderer {
    private def createLabel(fileName: String, color: Color, tooltip: String): JLabel = {
      val icon = new ImageIcon(Utilities.resource(fileName))
      new JLabel(icon) {
        setForeground(color)
        setToolTipText(tooltip)
        setHorizontalAlignment(SwingConstants.CENTER)
      }
    }

    val equal = createLabel("icons/equal.png", Color.BLACK, "Both values are equal")
    val notEqual = createLabel("icons/notEqual.png", Color.RED, "Values are not equal")
    val leftOnly = createLabel("icons/rightArrow.png", Color.MAGENTA, "Value is only in first collection")
    val rightOnly = createLabel("icons/leftArrow.png", Color.BLUE, "Value is only in seconds collection")

    def render(value: ComparisonResult.ComparisonResult) = {
      value match {
        case ComparisonResult.Equal => equal
        case ComparisonResult.NotEqual => notEqual
        case ComparisonResult.LeftOnly => leftOnly
        case ComparisonResult.RightOnly => rightOnly
      }
    }
  }

  abstract class Model(val underlying: Seq[ComparedItem]) extends TableModel {
    type ModelItem

    val columnNames = List("", title)

    def getColumnName(columnIndex: Int) = columnNames(columnIndex)

    def getColumnCount = columnNames.length

    def isCellEditable(rowIndex: Int, columnIndex: Int) = false

    def left: ModelItem => AnyRef

    def middle: ModelItem => ComparisonResult.ComparisonResult

    def getRowCount = underlying.size

    def getColumnClass(columnIndex: Int) = null // TODO specify return value

    def getValueAt(rowIndex: Int, columnIndex: Int) = {
      val row = underlying.apply(rowIndex).asInstanceOf[ModelItem]
      columnIndex match {
        case 0 => middle(row)
        case 1 => left(row)
      }
    }

    def setValueAt(aValue: Any, rowIndex: Int, columnIndex: Int) {}

    def addTableModelListener(l: TableModelListener) {}

    def removeTableModelListener(l: TableModelListener) {}
  }

  def model: Model = table.model.asInstanceOf[Model]

  def model_=(m: Model) {
    table.model = m
    val tcm = table.peer.getColumnModel
    tcm.getColumn(1).setResizable(false)
    tcm.getColumn(0).setResizable(false)
    tcm.getColumn(0).setWidth(20)
    tcm.getColumn(0).setMaxWidth(20)
    tcm.getColumn(0).setMinWidth(20)
  }

  val table = new Table {
    showGrid = false
    peer.setDefaultRenderer(ComparisonResult.Equal.getClass, new TableCellRenderer {
      def getTableCellRendererComponent(table: JTable, value: Any, isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int) = {
        ComparisonRenderer.render(value.asInstanceOf[ComparisonResult.ComparisonResult])
      }
    })
    //    peer.getTableHeader.setReorderingAllowed(false)

    //    override def rendererComponent(sel: Boolean, foc: Boolean, row: Int, col: Int) = {
    //      //FIND VALUE
    //      val v = model.getValueAt(
    //        peer.convertRowIndexToModel(row),
    //        peer.convertColumnIndexToModel(col))
    //      col match {
    //        case _ => tcr.componentFor(this, sel, foc, v, row, col)
    //      }
    //    }
  }
  contents = table
}