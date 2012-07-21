/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.model.semantic

object Historization {
  val undefined = Historization(-1, -1, -1, Undefined())
}

abstract case class Increment()

case class Incremental() extends Increment

case class Full() extends Increment

final case class Undefined() extends Increment

case class Historization(majorVersion: Int, minorVersion: Int, subtype: Int, increment: Increment) {
  override def toString = {
    if (majorVersion < 0 || minorVersion < 0 || subtype < 0 || increment == Undefined) "undefined"

    val b = new StringBuilder("SCD")
    if (majorVersion >= 0) b append majorVersion
    else b append " Undefined"
    if (minorVersion > 0) b append minorVersion
    if (subtype > 0) b append ("v" + subtype)
    b append(increment match {
      case Incremental() => "I"
      case Full() => "F"
      case Undefined() => "Undefined"
    })

    b.toString
  }
}

case class Scd0I() extends Historization(0, 0, 0, Incremental())

case class Scd0F() extends Historization(0, 0, 0, Full())

case class Scd1I() extends Historization(1, 0, 0, Incremental())

case class Scd1F() extends Historization(1, 0, 0, Full())

case class Scd2I() extends Historization(2, 0, 0, Incremental())

case class Scd2F() extends Historization(2, 0, 0, Full())

case class Scd25I(private val subtyp: Int) extends Historization(2, 5, subtyp, Incremental())

case class Scd25F(private val subtyp: Int) extends Historization(2, 5, subtyp, Full())