package mql.model.semantic

import java.lang.String
import collection.mutable

trait ColumnMapping extends Metadata with Comment {
  private var _mapping: Mapping = null
  def mapping = _mapping
  def mapping_=(mapping: Mapping) {
    _mapping = mapping
  }

  /**
   * Target column
   */
  def column: Column = _column
  def column_=(column: Column) {
    _column = column
  }
  private var _column: Column = null

  protected val prefix = "ColumnMapping."

  private def setMetadata(label: Any, value: Any) {
    metadata(prefix + label) = value
  }

  def apply(label: String): Any = metadata(prefix + label)

  def transformation: ColumnTransformation = apply("Transformation").asInstanceOf[() => ColumnTransformation]()

  def transformation_=(value: => ColumnTransformation) {
    setMetadata("Transformation", () => value)
  }
  transformation = ConstantTransformation("")

  @deprecated
  def mappedTo: mutable.MutableList[Column] = _mappedTo
  @deprecated
  def mappedTo_=(columnList: mutable.MutableList[Column]) {
    _mappedTo = columnList
  }

  private var _mappedTo = new mutable.MutableList[Column]
}