package mit.spbau.ru.ldvsoft.scala

sealed trait AST {
  def calculate(context: CalculationContext): Either[CalculationError, Double]
}

case class Literal(value: Double) extends AST {
  override def calculate(context: CalculationContext): Either[CalculationError, Double] = Right(value)
}

case class OperatorApplication(operator: InfixOperator, left: AST, right: AST) extends AST {
  override def calculate(context: CalculationContext): Either[CalculationError, Double] = {
    for {
      l <- left.calculate(context)
      r <- right.calculate(context)
      result <- operator.process(context, l, r)
    } yield result
  }
}
