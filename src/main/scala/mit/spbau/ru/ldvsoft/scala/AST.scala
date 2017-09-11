package mit.spbau.ru.ldvsoft.scala

sealed trait AST {
  def calculate(context: CalculationContext): Double
}

case class Literal(value: Double) extends AST {
  override def calculate(context: CalculationContext): Double = value
}

case class OperatorApplication(operator: InfixOperator, left: AST, right: AST) extends AST {
  override def calculate(context: CalculationContext): Double = {
    val l = left.calculate(context)
    val r = right.calculate(context)
    operator.process(context, l, r)
  }
}
