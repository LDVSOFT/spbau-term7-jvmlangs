package mit.spbau.ru.ldvsoft.scala

object DefaultOperators {
  val ADDITION_PRIORITY = 10
  val MULTIPLICATION_PRIORITY = 20

  object AdditionOperator extends Operator {
    override val name = "+"
    override val priority: Int = ADDITION_PRIORITY
    override val isLeftAssociative = true

    override def process(context: CalculationContext, left: Double, right: Double): Double = left + right
  }

  object SubtractionOperator extends Operator {
    override val name = "-"
    override val priority: Int = ADDITION_PRIORITY
    override val isLeftAssociative = true

    override def process(context: CalculationContext, left: Double, right: Double): Double = left - right
  }

  object MultiplicationOperator extends Operator {
    override val name = "*"
    override val priority: Int = MULTIPLICATION_PRIORITY
    override val isLeftAssociative = true

    override def process(context: CalculationContext, left: Double, right: Double): Double = left * right
  }

  object DivisionOperator extends Operator {
    override val name = "/"
    override val priority: Int = MULTIPLICATION_PRIORITY
    override val isLeftAssociative = true

    override def process(context: CalculationContext, left: Double, right: Double): Double = left / right
  }

  def addDefaultOperators(builder: Calculator.Builder): Boolean = {
    builder.addOperator(AdditionOperator) &&
      builder.addOperator(SubtractionOperator) &&
      builder.addOperator(MultiplicationOperator) &&
      builder.addOperator(DivisionOperator)
  }
}