/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.model.semantic

import collection.mutable.HashMap

trait Metadata {
  private[this] lazy val _metadata: collection.mutable.Map[Any, Any] = new HashMap[Any, Any]
  def metadata: collection.mutable.Map[Any, Any] = _metadata
}