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

object TokenizedTransformation {
  val extract = new {
    def unapply(t: TokenizedTransformation): Option[(String, Map[String, AliasedColumn])] = Some(t.sql, t.columnMap)
  }
}

case class NoTransformation(column: AliasedColumn)
  extends ColumnTransformation
  with Tokenized {
  def columns: Iterable[AliasedColumn] = List(column)
}