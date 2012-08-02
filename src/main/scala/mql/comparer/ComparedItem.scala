package mql.comparer

import ComparisonResult.ComparisonResult

case class ComparedItem(left: AnyRef, middle: ComparisonResult, right: AnyRef)
