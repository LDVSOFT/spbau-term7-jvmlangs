package mit.spbau.ru.ldvsoft.scala

import scala.collection.mutable

class CalculationContext(val infixOperators: Map[String, InfixOperator])

object CalculationContext {
  class Builder {
    private val operators = mutable.Map[String, InfixOperator]()

    def build(): CalculationContext = new CalculationContext(Map(operators.toSeq: _*))

    def addOperator(operator: InfixOperator): Boolean = {
      if (operators contains operator.name)
        return false
      operators.put(operator.name, operator).isEmpty
    }
  }
}
