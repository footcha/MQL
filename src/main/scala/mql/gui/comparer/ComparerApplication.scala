package mql.gui.comparer

import swing._
import swing.event.Key
import javax.swing._
import java.awt.Color
import swing.Action
import collection.mutable.ListBuffer
import mql.comparer._
import mql.comparer.ComparedItem
import scala.Some
import swing.event.TableRowsSelected
import swing.TabbedPane.Page
import table.TableCellRenderer

object ComparerApplication extends SimpleSwingApplication {

  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)

  val mapDetail = new CustomComparisonTable("Table mapping attributes")
  val columnsList = new CustomComparisonTable("Column mappings") {
    table.peer.setDefaultRenderer(classOf[XlsColumnMapping], new TableCellRenderer {
      def getTableCellRendererComponent(table: JTable, value: Any, isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int) = {
        val txt = value match {
          case v: XlsColumnMapping => v.columnCode
        }
        new JLabel(txt) {
          setBackground(if (isSelected) table.getSelectionBackground else table.getBackground)
          setOpaque(true)
        }
      }
    })
  }
  val columnDetail = new CustomComparisonTable("Column mapping detail")
  val fieldComparison = new ScrollPane(
    new Label {
      opaque = true
      background = Color.WHITE
      verticalAlignment = Alignment.Top
      horizontalAlignment = Alignment.Left
    }) {
    def label = this.contents.head.asInstanceOf[Label]

    def text = label.text

    def text_=(txt: String) {
      label.text = txt
    }
  }

  val mappingsList = new CustomComparisonTable("Mappings")

  def top = new Frame {
    title = "MQL Comparer 1.0.53"
    iconImage = toolkit.getImage(Utilities.resource("icons/logo32.png"))

    import javax.swing.WindowConstants.EXIT_ON_CLOSE

    peer.setDefaultCloseOperation(EXIT_ON_CLOSE)

    initMenuBar(this)

    val mainPane = new SplitPane(Orientation.Vertical) {
      topComponent = mappingsList
      bottomComponent = new SplitPane {
        topComponent = new TabbedPane {
          pages += new Page("Table Mapping", mapDetail)
          pages += new Page("Column Mapping", new SplitPane(Orientation.Vertical) {
            topComponent = columnsList
            bottomComponent = columnDetail
          })
        }
        bottomComponent = new BoxPanel(Orientation.Vertical) {
          _contents += new Label("Field details and differences") {
            this.horizontalTextPosition = Alignment.Left
            this.horizontalAlignment = Alignment.Left
          }
          _contents += fieldComparison
        }
      }
    }
    contents_=(mainPane)
    mainPane.listenTo(mappingsList.table.selection)
    mainPane.reactions += {
      case TableRowsSelected(source, range, false) => {
        val rowIdx = source.peer.getSelectedRow
        if (rowIdx >= 0) mappingDetail(rowIdx)
      }
    }
    columnDetail.listenTo(columnsList.table.selection)
    columnDetail.reactions += {
      case TableRowsSelected(source, range, false) => {
        val rowIdx = source.peer.getSelectedRow
        if (rowIdx >= 0) {
          val current = columnsList.items.toList(rowIdx)
          val left = current.left.asInstanceOf[XlsColumnMapping]
          val right = current.right.asInstanceOf[XlsColumnMapping]
          val labels = nonEmpty(left, right).labels.cells
          val emptyCell = (0, "")
          val det = left.cells.toSeq.sortBy(_._1) zipAll(right.cells.toSeq.sortBy(_._1), emptyCell, emptyCell) map (it => {
            val left = it._1._2
            val right = it._2._2
            val label = labels(if (it._1._2 != "") it._1._1 else it._2._1)
            ComparedItem(label, comparisonResult(left, right)(""), label)
          })
          columnDetail.items = det
        }
      }
    }
    fieldComparison.listenTo(mapDetail.table.selection)
    fieldComparison.listenTo(columnDetail.table.selection)
    fieldComparison.reactions += {
      case TableRowsSelected(`mapDetail`.table, range, false) => {
        val rowIdx = mapDetail.table.peer.getSelectedRow
        if (rowIdx >= 0) {
          val x = mappingsList.table.peer.getSelectedRow
          val l = asXls(mappingsList.items(x).left).cells(rowIdx)
          val r = asXls(mappingsList.items(x).right).cells(rowIdx)
          val diff = new diff_match_patch()
          val html = diff.diff_prettyHtml(diff.diff_main(l, r))
          fieldComparison.text = "<html>%s</html>" format html
        }
      }
      case TableRowsSelected(`columnDetail`.table, range, false) => {
        val rowIdx = columnDetail.table.peer.getSelectedRow
        if (rowIdx >= 0) {
          val x = columnsList.table.peer.getSelectedRow
          val l = columnsList.items(x).left.asInstanceOf[XlsColumnMapping].cells(rowIdx)
          val r = columnsList.items(x).right.asInstanceOf[XlsColumnMapping].cells(rowIdx)
          val diff = new diff_match_patch()
          val html = diff.diff_prettyHtml(diff.diff_main(l, r))
          fieldComparison.text = "<html>%s</html>" format html
        }
      }
    }
  }

  def initMenuBar(frame: Frame) {
    frame.menuBar = new MenuBar {
      this.contents += new Menu("File") {
        mnemonic = Key.F
        contents += new MenuItem("Open") {
          mnemonic = Key.O
          action = new Action("Open") {
            accelerator = Some(KeyStroke.getKeyStroke("control X"))

            def apply() {
              println("hit open.")
            }
          }
        }
        contents += new MenuItem("Close") {
          mnemonic = Key.Q
          action = new Action("Close") {
            def apply() {
              quit()
            }
          }
        }
      }
      contents += new Menu("View") {
        mnemonic = Key.V
        contents += new MenuItem("Reload") {
          mnemonic = Key.R
          action = new Action("Reload") {
            accelerator = Some(KeyStroke.getKeyStroke("F5"))

            def apply() {
              mappingsList.items = comparedMappings()
            }
          }
        }
      }
      contents += new Menu("Help") {
        mnemonic = Key.H
        contents += new MenuItem("About") {
          mnemonic = Key.A
          action = new Action("About") {
            def apply() {

            }
          }
        }
      }
    }
  }

  var files = ("", "")

  def comparedMappings(): List[ComparedItem] = {
    val xls = new XlsReader(files).read()
    val out = new ListBuffer[ComparedItem]
    val comparer = new CollectionComparer(xls._1, xls._2) {
      different = (m1, m2) => out += ComparedItem(m1, ComparisonResult.NotEqual, m2)
      same = (m1, m2) => out += ComparedItem(m1, ComparisonResult.Equal, m2)
      inSecond = m => out += ComparedItem(m, ComparisonResult.LeftOnly, XlsTableMapping.None)
      inFirst = m => out += ComparedItem(XlsTableMapping.None, ComparisonResult.RightOnly, m)
    }
    comparer.compare(compareTableMapping, interruptor = true)
    mapDetail.items = List()
    fieldComparison.text = ""
    out.toList
  }

  protected def compareTableMapping(m1: XlsTableMapping, m2: XlsTableMapping): Boolean = {
    if (!compareRow(m1, m2)) return false
    var areColumnsEqual = true
    val columnComparer = new CollectionComparer(m1.columns, m2.columns) {
      different = (c1, c2) => areColumnsEqual = false
      inFirst = c => areColumnsEqual = false
      inSecond = c => areColumnsEqual = false
    }
    columnComparer.compare(compareRow, interruptor = areColumnsEqual)
    areColumnsEqual
  }

  protected def compareRow[T <: XlsRow](left: T, right: T): Boolean = {
    if (left.cells.size != right.cells.size) {
      return false
    }
    for (val i <- 0 until left.cells.size) {
      if (left.cells(i) != right.cells(i)) return false
    }
    true
  }

  def asXls(obj: Any): XlsTableMapping = obj.asInstanceOf[XlsTableMapping]

  def comparisonResult[T](x: T, y: T)(emptyCell: T) = (x, y) match {
    case (l, r) if l == r => ComparisonResult.Equal
    case (_, `emptyCell`) => ComparisonResult.LeftOnly
    case (`emptyCell`, _) => ComparisonResult.RightOnly
    case _ => ComparisonResult.NotEqual
  }

  def mappingDetail(rowIdx: Int) {
    val item = mappingsList.items(rowIdx)
    val left = asXls(item.left)
    val right = asXls(item.right)
    val labels = nonEmpty(left, right).labels.cells
    val emptyCell = (0, "")
    val det = left.cells.toSeq.sortBy(_._1) zipAll(right.cells.toSeq.sortBy(_._1), emptyCell, emptyCell) map (it => {
      //      val nonEmpty =
      val left = it._1._2
      val right = it._2._2
      val label = labels(if (it._1._2 != "") it._1._1 else it._2._1)
      ComparedItem(label, comparisonResult(left, right)(emptyCell._2), label)
    })
    mapDetail.items = det

    val out = new ListBuffer[ComparedItem]
    val columnComparer = new CollectionComparer(left.columns, right.columns) {
      same = (c1, c2) => out += ComparedItem(c1, ComparisonResult.Equal, c2)
      different = (c1, c2) => out += ComparedItem(c1, ComparisonResult.NotEqual, c2)
      inFirst = c => out += ComparedItem(c, ComparisonResult.LeftOnly, XlsColumnMapping.None)
      inSecond = c => out += ComparedItem(XlsColumnMapping.None, ComparisonResult.RightOnly, c)
    }
    columnComparer.compare(compareRow, interruptor = true)

    columnsList.items = out
    fieldComparison.text = ""
  }

  private def nonEmpty[T](x: T, y: T) = if (!x.isInstanceOf[Empty]) x else y
}