package mql.comparer

import collection.mutable

class CollectionComparer[K, T](first: mutable.HashMap[K, T], second: mutable.HashMap[K, T]) {
  private val noop1: T => Unit = m => Unit
  private val noop2: (T, T) => Unit = (m1, m2) => Unit
  var inFirst = noop1
  var inSecond = noop1
  var different = noop2
  var same = noop2

  def compare(comparer: (T, T) => Boolean, interruptor: => Boolean) {
    val secondTmp = second.clone()
    for (val (key, val1) <- first if interruptor) {
      secondTmp.remove(key) match {
        case Some(val2) => {
          if (comparer(val1, val2)) same(val1, val2)
          else different(val1, val2)
        }
        case None => inFirst(val1)
      }
    }
    if (interruptor) for (val (key, mapping2) <- secondTmp if interruptor) inSecond(mapping2)
  }
}
