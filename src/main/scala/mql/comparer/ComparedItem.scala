/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.comparer

import ComparisonResult.ComparisonResult

case class ComparedItem(left: AnyRef, middle: ComparisonResult, right: AnyRef)
