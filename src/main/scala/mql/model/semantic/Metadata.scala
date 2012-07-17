package mql.model.semantic

import collection.mutable.HashMap

trait Metadata {
  private[this] lazy val _metadata: collection.mutable.Map[Any, Any] = new HashMap[Any, Any]
  def metadata: collection.mutable.Map[Any, Any] = _metadata
}