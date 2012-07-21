/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
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