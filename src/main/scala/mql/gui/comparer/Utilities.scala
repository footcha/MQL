package mql.gui.comparer

object Utilities {
  def resource(name: String) = ClassLoader.getSystemClassLoader.getResource(name)
}