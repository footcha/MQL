/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.model.semantic.edw.tokens

import mql.model.semantic.{ColumnTransformation, SourceSystem}

case class Generate() extends ColumnTransformation

case class SourceSystemTransformation(system: SourceSystem) extends ColumnTransformation
