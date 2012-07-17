package mql.model.semantic

import java.lang.String

object SqlConvertible {
  implicit def stringToSql(from: () => String): SqlConvertible = {
    new SqlConvertible {
      def toSql = from()
    }
  }

  implicit def stringToSql(from: String): SqlConvertible = {
    new SqlConvertible {
      def toSql = from
    }
  }
}

trait SqlConvertible{
  def toSql: String
}