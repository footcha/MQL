package mql.comparer

import collection.mutable
import org.apache.poi.ss.usermodel.Row
import java.io.FileInputStream
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.{Cell => PoiCell}
import java.util

class XlsTableMapping(val labels: XlsRow) extends XlsRow(labels) {
  val columns = new mutable.HashMap[String, XlsColumnMapping]

  def key = cells(2)
}

class XlsColumnMapping(val labels: XlsRow) extends XlsRow(labels) {
  def key = cells(3)

  def columnCode = cells(5)
}

abstract class XlsRow(labels: XlsRow) {
  val cells = new mutable.HashMap[Int, String]

  def apply(label: String): String = {
    val idx = labels.cells.find(c => c._2 == label).get._1
    cells(idx)
  }

  def key: String
}

class XlsReader(val files: (String, String)) {
  def read() = (createStructure(files._1), createStructure(files._2))

  protected def cellValue(cell: PoiCell): String = {
    if (cell == null) ""
    else {
      cell.getCellType match {
        case PoiCell.CELL_TYPE_STRING => cell.getStringCellValue
        case PoiCell.CELL_TYPE_NUMERIC => cell.getNumericCellValue.toString
        case PoiCell.CELL_TYPE_BLANK => ""
        case PoiCell.CELL_TYPE_FORMULA => cell.getCellFormula // TODO
        case e => sys.error(e.toString)
      }
    }
  }

  protected def readRow[T <: XlsRow](iterator: util.Iterator[Row], creator: => T): T = {
    val row = iterator.next()
    val newRow = creator
    for (val cellIdx <- row.getFirstCellNum until row.getLastCellNum) {
      val cell = row.getCell(cellIdx)
      newRow.cells += (cellIdx -> cellValue(cell))
    }
    newRow
  }

  private def createStructure(fileName: String): mutable.HashMap[String, XlsTableMapping] = {
    val file = new FileInputStream(fileName)
    val wb = new HSSFWorkbook(file)
    file.close()
    val tableMap = wb.getSheet("TableMap")

    var iter = tableMap.rowIterator()
    val head = readRow(iter, new XlsTableMapping(null))
    val tableMappings = new mutable.HashMap[String, XlsTableMapping]()
    while (iter.hasNext) {
      val row = readRow(iter, new XlsTableMapping(head))
      tableMappings(row.key) = row
    }

    val columnMap = wb.getSheet("ColMap")
    iter = columnMap.rowIterator()
    val head2 = readRow(iter, new XlsColumnMapping(null))
    while (iter.hasNext) {
      val row = readRow(iter, new XlsColumnMapping(head2))
      row.cells.get(0) match {
        case Some(_) => tableMappings(row.key).columns += ((row.columnCode, row))
        case None =>
      }

    }
    tableMappings
  }
}