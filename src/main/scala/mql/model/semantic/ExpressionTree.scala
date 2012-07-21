/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.model.semantic

import collection.mutable
import collection.mutable.ArrayBuffer
import collection.generic.SeqForwarder

abstract case class ExpressionTree() { thisNode =>

  private[this] var _parent: ExpressionTree = thisNode

  /**
   * Gets parent node of this node.
   * If this node has no parent then parent() returns this node.
   */
  def parent: ExpressionTree = _parent

  private def parent_=(parent: ExpressionTree) {
    _parent = parent
  }

  val children = new mutable.ArrayBuilder[ExpressionTree]
    with SeqForwarder[ExpressionTree]
  {
    private[this] val _underlying = new ArrayBuffer[ExpressionTree]

    def +=(elem: ExpressionTree): this.type = {
      _underlying += elem
      elem.parent = thisNode
      this
    }

    def clear() {
      _underlying.clear()
    }

    def result(): Array[ExpressionTree] = underlying.toArray

    protected override def underlying: Seq[ExpressionTree] = _underlying
  }
}
