/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.model.semantic.edw

import mql.model.semantic.{Node, ConstantNode, SourceSystem}

object Generate extends Node

sealed case class SourceSystemNode(system: SourceSystem) extends ConstantNode

