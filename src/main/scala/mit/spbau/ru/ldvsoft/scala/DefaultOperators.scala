package mit.spbau.ru.ldvsoft.scala

object DefaultOperators {
  val ADDITION_PRIORITY = 10
  val MULTIPLICATION_PRIORITY = 20

  object Addition extends InfixOperator {
    override val name = "+"
    override val priority: Int = ADDITION_PRIORITY
    override val isLeftAssociative = true

    override def process(context: CalculationContext, left: Double, right: Double): Either[CalculationError, Double] =
      Right(left + right)
  }

  object Subtraction extends InfixOperator {
    override val name = "-"
    override val priority: Int = ADDITION_PRIORITY
    override val isLeftAssociative = true

    override def process(context: CalculationContext, left: Double, right: Double): Either[CalculationError, Double] =
      Right(left - right)
  }

  object Multiplication extends InfixOperator {
    override val name = "*"
    override val priority: Int = MULTIPLICATION_PRIORITY
    override val isLeftAssociative = true

    override def process(context: CalculationContext, left: Double, right: Double): Either[CalculationError, Double] =
      Right(left * right)
  }

  object Division extends InfixOperator {
    override val name = "/"
    override val priority: Int = MULTIPLICATION_PRIORITY
    override val isLeftAssociative = true

    override def process(context: CalculationContext, left: Double, right: Double): Either[CalculationError, Double] = {
      if (right == .0)
        Left(new MathError("Division by zero"))
      else
        Right(left / right)
    }
  }

  def addDefaultOperators(builder: CalculationContext.Builder): Boolean = {
    builder.addOperator(Addition) &&
      builder.addOperator(Subtraction) &&
      builder.addOperator(Multiplication) &&
      builder.addOperator(Division)
  }
}