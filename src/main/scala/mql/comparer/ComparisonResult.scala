package mql.comparer

object ComparisonResult extends Enumeration {
  type ComparisonResult = Value
  val LeftOnly, RightOnly, Equal, NotEqual = Value
}
