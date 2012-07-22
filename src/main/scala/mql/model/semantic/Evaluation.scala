/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.model.semantic

case class Evaluation(code: Any)

object StandardEvaluation {
  val simple = Evaluation("Low")
  val lowerDifficult = Evaluation("Middle low")
  val difficult = Evaluation("Middle difficult")
  val higherDifficult  = Evaluation("Difficult")
  val complex = Evaluation("Very complex")
}