package mql.model.semantic

abstract case class ColumnTransformation()

trait Tokenized {
  def columns: Iterable[AliasedColumn]
}

case class ConstantTransformation(constant: String) extends ColumnTransformation
case class TokenizedTransformation(sql: String, columnsSeq: (String, AliasedColumn)*)
  extends ColumnTransformation with Tokenized {
  lazy val columnMap = columnsSeq.toMap
  lazy val columns= columnMap.values
}

case class NoTransformation(column: AliasedColumn)
  extends ColumnTransformation
  with Tokenized {
  def columns: Iterable[AliasedColumn] = List(column)
}