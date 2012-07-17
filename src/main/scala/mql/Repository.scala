package mql

import collection.mutable.{ListBuffer, Buffer}

trait Repository[T] extends Iterable[T] {
  protected val repository: Buffer[T] = new ListBuffer[T]

  def register(f: => T) {
    repository += f
  }

  def iterator = repository.iterator
}