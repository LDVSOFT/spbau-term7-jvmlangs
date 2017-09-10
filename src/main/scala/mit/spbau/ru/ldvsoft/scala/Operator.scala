package mit.spbau.ru.ldvsoft.scala

trait Operator {
  val name: String
  val priority: Int
  val isLeftAssociative: Boolean
  def process(context: CalculationContext, left: Double, right: Double): Double

  override def toString = s"(Operator $name)"
}
