package mql.comparer

import collection.mutable
import org.slf4j.LoggerFactory

class CollectionComparer[K, T](first: mutable.HashMap[K, T], second: mutable.HashMap[K, T]) {
  private val logger = LoggerFactory.getLogger(classOf[CollectionComparer[_, _]])
  private val noop1: T => Unit = m => Unit
  private val noop2: (T, T) => Unit = (m1, m2) => Unit
  var inFirst = noop1
  var inSecond = noop1
  var different = noop2
  var same = noop2

  def compare(comparer: (T, T) => Boolean, interruptor: => Boolean) {
    logger.debug("Starting comparison of collections of sizes {}, {}", first.size, second.size)
    val secondTmp = second.clone()
    for (val (key, val1) <- first if interruptor) {
      secondTmp.remove(key) match {
        case Some(val2) => {
          if (comparer(val1, val2)) {
            logger.debug("Objects in both collections are equal for key {}", key)
            same(val1, val2)
          }
          else {
            logger.debug("Objects in both collections are not equal for key {}", key)
            different(val1, val2)
          }
        }
        case None => {
          logger.debug("In first collection only for key {}", key)
          inFirst(val1)
        }
      }
    }
    if (interruptor) for (val (key, mapping2) <- secondTmp if interruptor) {
      logger.debug("In second collection only for key {}", key)
      inSecond(mapping2)
    }
  }
}