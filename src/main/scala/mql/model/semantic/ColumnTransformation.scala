/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.model.semantic

abstract case class ColumnTransformation()

trait Tokenized {
  def columns: Iterable[AliasedColumn]
}

case class ConstantTransformation(constant: String) extends ColumnTransformation
case class TokenizedTransformation(sql: String, columnsSeq: (Symbol, AliasedColumn)*)
  extends ColumnTransformation with Tokenized {
  lazy val columnMap = columnsSeq.toMap
  lazy val columns= columnMap.values
}

object TokenizedTransformation {
  val extract = new {
    def unapply(t: TokenizedTransformation): Option[(String, Seq[(Symbol, AliasedColumn)])] = Some(t.sql, t.columnMap.toSeq)
  }
}

case class NoTransformation(column: AliasedColumn)
  extends ColumnTransformation
  with Tokenized {
  def columns: Iterable[AliasedColumn] = List(column)
}