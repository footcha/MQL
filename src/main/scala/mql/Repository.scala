/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql

import collection.mutable.{ListBuffer, Buffer}

trait Repository[T] extends Iterable[T] {
  protected val repository: Buffer[T] = new ListBuffer[T]

  def register(f: => T) {
    repository += f
  }

  def iterator = repository.iterator
}