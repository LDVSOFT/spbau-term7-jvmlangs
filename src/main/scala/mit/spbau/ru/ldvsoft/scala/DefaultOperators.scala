package mit.spbau.ru.ldvsoft.scala

object DefaultOperators {
  val ADDITION_PRIORITY = 10
  val MULTIPLICATION_PRIORITY = 20

  object Addition extends InfixOperator {
    override val name = "+"
    override val priority: Int = ADDITION_PRIORITY
    override val isLeftAssociative = true

    override def process(context: CalculationContext, left: Double, right: Double): Double = left + right
  }

  object Subtraction extends InfixOperator {
    override val name = "-"
    override val priority: Int = ADDITION_PRIORITY
    override val isLeftAssociative = true

    override def process(context: CalculationContext, left: Double, right: Double): Double = left - right
  }

  object Multiplication extends InfixOperator {
    override val name = "*"
    override val priority: Int = MULTIPLICATION_PRIORITY
    override val isLeftAssociative = true

    override def process(context: CalculationContext, left: Double, right: Double): Double = left * right
  }

  object Division extends InfixOperator {
    override val name = "/"
    override val priority: Int = MULTIPLICATION_PRIORITY
    override val isLeftAssociative = true

    override def process(context: CalculationContext, left: Double, right: Double): Double = left / right
  }

  def addDefaultOperators(builder: CalculationContext.Builder): Boolean = {
    builder.addOperator(Addition) &&
      builder.addOperator(Subtraction) &&
      builder.addOperator(Multiplication) &&
      builder.addOperator(Division)
  }
}