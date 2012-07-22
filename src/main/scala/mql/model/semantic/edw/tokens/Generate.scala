/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.model.semantic.edw.tokens

import mql.model.semantic.{ConstantExpression, CommandExpression, SourceSystem}

object Generate extends CommandExpression

case class SourceSystemTransformation(system: SourceSystem) extends ConstantExpression(system.code)
