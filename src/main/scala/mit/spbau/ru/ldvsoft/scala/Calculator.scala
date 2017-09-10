package mit.spbau.ru.ldvsoft.scala

import scala.collection.mutable

class Calculator(val calculationContext: CalculationContext) {
  def apply(input: String): Either[CalculationError, Double] = {
    val lexer = new Lexer()
    val parser = new Parser(calculationContext)
    for {
      lexems <- lexer(input)
      parsed <- parser(lexems)
      result = parsed.calculate(calculationContext)
    } yield result
  }
}

object Calculator {
  class Builder {
    private val operators = mutable.Map[String, Operator]()

    def build(): Calculator = new Calculator(new CalculationContext(Map(operators.toSeq: _*)))

    def addOperator(operator: Operator): Boolean = {
      if (operators contains operator.name)
        return false
      operators.put(operator.name, operator).isEmpty
    }
  }
}
