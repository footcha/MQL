/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.gui.comparer

import swing._
import swing.event.{SelectionChanged, Key, TableRowsSelected}
import javax.swing._
import java.awt.Color
import swing.Action
import collection.mutable.ListBuffer
import mql.comparer._
import mql.comparer.ComparedItem
import scala.Some
import swing.TabbedPane.Page
import table.TableCellRenderer
import mql.Version
import java.lang.Thread.UncaughtExceptionHandler
import org.slf4j.LoggerFactory
import swing.Dialog.Message
import org.slf4j.impl.StaticLoggerBinder
import ch.qos.logback.classic.LoggerContext

object SelectedRow {
  def unapply(table: Table): Option[Int] = Some(table.peer.getSelectedRow)
}

object ComparerApplication extends SimpleSwingApplication {
  private val logger = LoggerFactory.getLogger(getClass)

  object ErrorConsole extends Dialog {
    title = "Error console"

    val txt = new TextArea {
      rows = 20
      columns = 50
      font = font.deriveFont(0, (font.getSize * 1.5).round)
    }
    contents_=(new ScrollPane(txt))
  }

  Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler {
    def getFullMessage(e: Throwable) = {
      val b = new StringBuilder
      var exTmp = e
      while (exTmp != null) {
        b append "Caused by " append exTmp.getClass append ": " append exTmp.getMessage append "\n"
        b appendAll exTmp.getStackTrace.flatMap(st => {
          "\t at %s.%s (%s:%d)\n" format(st.getClassName, st.getMethodName, st.getFileName, st.getLineNumber)
        })
        exTmp = exTmp.getCause
      }
      b.toString()
    }

    def uncaughtException(t: Thread, e: Throwable) {
      logger.error("Application error.", e)
      ErrorConsole.txt.text += getFullMessage(e)
      ErrorConsole.visible = true
    }
  })

  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)

  val tableMappingDetail = new CustomComparisonTable("Table mapping attributes")
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

  val mappingsList = new CustomComparisonTable("Mappings")

  def createFieldDetail(title: String) = new BoxPanel(Orientation.Vertical) {
    private val pane = new ScrollPane(
      new Label {
        opaque = true
        background = Color.WHITE
        verticalAlignment = Alignment.Top
        horizontalAlignment = Alignment.Left
      }) {

      def label = this.contents.head.asInstanceOf[Label]
    }

    def content = pane.label

    _contents += new Label(title) {
      this.horizontalTextPosition = Alignment.Left
      this.horizontalAlignment = Alignment.Left
    }

    _contents += pane
  }

  def top = topFrame

  val topFrame = new Frame {

    import javax.swing.WindowConstants.EXIT_ON_CLOSE

    peer.setDefaultCloseOperation(EXIT_ON_CLOSE)

    title = "MQL Comparer %s" format Version.version
    iconImage = toolkit.getImage(Utilities.resource("icons/logo32.png"))

    initMenuBar(this)

    val fieldDetail = new BoxPanel(Orientation.Vertical) {
      val tableMapping = createFieldDetail("Table mapping field details and differences")
      val columnMapping = createFieldDetail("Column mapping field details and differences")
      _contents += tableMapping
      _contents += columnMapping
      activateTableMappingField()

      def activateTableMappingField() {
        tableMapping.visible = true
        columnMapping.visible = false
      }

      def activateColumnMappingField() {
        tableMapping.visible = false
        columnMapping.visible = true
      }

      def initReactions() {
        initReactions(tableMappingDetail, tableMapping.content, mappingsList)
        initReactions(columnDetail, columnMapping.content, columnsList)
      }

      private def initReactions[T <: XlsRow](source: CustomComparisonTable, listener: Label, list: CustomComparisonTable) {
        listener.listenTo(source.table.selection)
        listener.reactions += {
          case TableRowsSelected(source@SelectedRow(rowIdx), range, false) if (rowIdx >= 0) => {
            val x = list.table.peer.getSelectedRow
            val l = list.items(x).left.asInstanceOf[T].cells(rowIdx)
            val r = list.items(x).right.asInstanceOf[T].cells(rowIdx)
            val diff = new diff_match_patch()
            val html = diff.diff_prettyHtml(diff.diff_main(l, r))
            listener.text = "<html>%s</html>" format html
          }
        }
      }
    }

    val mainPane = new SplitPane(Orientation.Vertical) {
      val tabbedPane = new TabbedPane {
        pages += new Page("Table Mapping", tableMappingDetail)
        pages += new Page("Column Mapping", new SplitPane(Orientation.Vertical) {
          topComponent = columnsList
          bottomComponent = columnDetail
        })
      }
      topComponent = mappingsList
      bottomComponent = new SplitPane {
        topComponent = tabbedPane
        bottomComponent = fieldDetail
      }
    }
    contents_=(mainPane)
    mainPane.listenTo(mappingsList.table.selection)
    mainPane.reactions += {
      case TableRowsSelected(source@SelectedRow(rowIdx), range, false) if (rowIdx >= 0) => {
        mappingDetail(rowIdx)
        fieldDetail.tableMapping.content.text = ""
        fieldDetail.columnMapping.content.text = ""
      }
    }
    columnDetail.listenTo(columnsList.table.selection)

    columnDetail.reactions += {
      case TableRowsSelected(source@SelectedRow(rowIdx), range, false) if (rowIdx >= 0) => {
        val current = columnsList.items.toList(rowIdx)
        columnDetail.items = createModelItems(current)
      }
    }
    fieldDetail.initReactions()
    fieldDetail.tableMapping.listenTo(mainPane.tabbedPane.selection)
    fieldDetail.tableMapping.reactions += {
      case SelectionChanged(`mainPane`.tabbedPane) => {
        mainPane.tabbedPane.selection.index match {
          case 0 => fieldDetail.activateTableMappingField()
          case 1 => fieldDetail.activateColumnMappingField()
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
            accelerator = Some(KeyStroke.getKeyStroke("control O"))

            lazy val fileChooser = new TwoFilesChooser(frame)

            def apply() {
              fileChooser.show() match {
                case Dialog.Result.Ok => {
                  files = fileChooser.files
                  reload()
                }
                case _ => println("Files not committed.")
              }
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
              reload()
            }
          }
        }
      }
      contents += new Menu("Help") {
        mnemonic = Key.H
        contents += new MenuItem("About") {
          mnemonic = Key.A
          action = new Action("About") {
            accelerator = Some(KeyStroke.getKeyStroke("F1"))

            def apply() {
              About.visible = true
            }
          }
        }
      }
    }
  }

  private def reload() {
    mappingsList.items = {
      tableMappingDetail.items = List()
      topFrame.fieldDetail.tableMapping.content.text = ""
      topFrame.fieldDetail.columnMapping.content.text = ""
      comparedMappings()
    }
  }

  var files = ("", "")

  def comparedMappings(): List[ComparedItem] = {
    val xls = new XlsReader(files).read()
    logger.debug("Starting comparing mappings")
    val out = new ListBuffer[ComparedItem]
    val comparer = new CollectionComparer(xls._1, xls._2) {
      different = (m1, m2) => out += ComparedItem(m1, ComparisonResult.NotEqual, m2)
      same = (m1, m2) => out += ComparedItem(m1, ComparisonResult.Equal, m2)
      inSecond = m => out += ComparedItem(m, ComparisonResult.LeftOnly, XlsTableMapping.None)
      inFirst = m => out += ComparedItem(XlsTableMapping.None, ComparisonResult.RightOnly, m)
    }
    comparer.compare(compareTableMapping, interruptor = true)
    logger.debug("Finished comparing mappings")
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

  def mappingDetail(rowIdx: Int) {
    val item = mappingsList.items(rowIdx)
    tableMappingDetail.items = createModelItems(item)
    val left = item.left.asInstanceOf[XlsTableMapping]
    val right = item.right.asInstanceOf[XlsTableMapping]
    val out = new ListBuffer[ComparedItem]
    val columnComparer = new CollectionComparer(left.columns, right.columns) {
      same = (c1, c2) => out += ComparedItem(c1, ComparisonResult.Equal, c2)
      different = (c1, c2) => out += ComparedItem(c1, ComparisonResult.NotEqual, c2)
      inFirst = c => out += ComparedItem(c, ComparisonResult.LeftOnly, XlsColumnMapping.None)
      inSecond = c => out += ComparedItem(XlsColumnMapping.None, ComparisonResult.RightOnly, c)
    }
    columnComparer.compare(compareRow, interruptor = true)

    columnsList.items = out
    columnDetail.items = List()
  }

  def createModelItems[TItem <: XlsRow](current: ComparedItem) = {
    def nonEmptyFcn[X](x: X, y: X)(a: Any, b: Any): Any =
      (if (!x.isInstanceOf[Empty]) x else y) match {
        case `x` => a
        case `y` => b
      }
    def comparisonResult(x: String, y: String)(emptyCell: String) = (x, y) match {
      case (_, `emptyCell`) => ComparisonResult.LeftOnly
      case (`emptyCell`, _) => ComparisonResult.RightOnly
      case (l, r) if l == r => ComparisonResult.Equal
      case _ => ComparisonResult.NotEqual
    }
    val left = current.left.asInstanceOf[TItem]
    val right = current.right.asInstanceOf[TItem]
    val nonEmptyResolver = nonEmptyFcn(left, right) _
    val labels = nonEmptyResolver(left, right).asInstanceOf[TItem].labels.cells
    val emptyCell = (0, "FAKE_CONSTANT")
    left.cells.toSeq.sortBy(_._1) zipAll(right.cells.toSeq.sortBy(_._1), emptyCell, emptyCell) map (it => {
      val left = it._1._2
      val right = it._2._2
      val label = labels(nonEmptyResolver(it._1._1, it._2._1).asInstanceOf[Int])
      ComparedItem(label, comparisonResult(left, right)(emptyCell._2), label)
    })
  }
}