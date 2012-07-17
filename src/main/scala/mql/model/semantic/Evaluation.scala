package mql.model.semantic

case class Evaluation(val code: Any)

object StandardEvaluation {
  val simple = Evaluation("Low")
  val lowerDifficult = Evaluation("Middle low")
  val difficult = Evaluation("Middle difficult")
  val higherDifficult  = Evaluation("Difficult")
  val complex = Evaluation("Very complex")
}