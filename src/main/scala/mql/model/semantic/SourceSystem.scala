package mql.model.semantic

import mql.Repository

case class SourceSystem(code: String)

object StandardSourceSystem extends StandardSourceSystem {
  val undefined = SourceSystem("Undefined")
}

class StandardSourceSystem extends Repository[SourceSystem] {
  def findByName(code: String): SourceSystem =
    repository.find(sourceSystem => sourceSystem.code == code) match {
      case Some(t) => t
      case None => sys.error("Source system with code [" + code + "] was not found")
    }

  val man = SourceSystem("MAN")
  register(man)
}