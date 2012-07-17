package mql

object Checker {
  def withNotNull[T](obj: T)(function: => Unit) {
    assert(obj != null)
    function
  }
}