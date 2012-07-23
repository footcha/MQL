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

abstract class Node { thisNode =>

  private[this] var _parent: Node = thisNode

  /**
   * Gets parent node of this node.
   * If this node has no parent then parent() returns this node.
   */
  def parent: Node = _parent

  private def parent_=(parent: Node) {
    _parent = parent
  }

  val children = new mutable.ArrayBuilder[Node]
    with SeqForwarder[Node]
  {
    private[this] val _underlying = new ArrayBuffer[Node]

    def +=(elem: Node): this.type = {
//      if (elem == null) throw new NullPointerException("Null element is forbidden as child in expression tree.")
      _underlying += elem
      elem.parent = thisNode
      this
    }

    def clear() {
      _underlying.clear()
    }

    def result(): Array[Node] = underlying.toArray

    protected override def underlying: Seq[Node] = _underlying
  }
}