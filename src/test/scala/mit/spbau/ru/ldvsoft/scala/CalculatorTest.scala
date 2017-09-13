package mit.spbau.ru.ldvsoft.scala

import org.scalatest.FunSuite

class CalculatorTest extends FunSuite {
  private def getCalculator: Calculator = {
    val builder = new CalculationContext.Builder()
    DefaultOperators.addDefaultOperators(builder)
    new Calculator(builder.build())
  }

  test("Calculator works properly on simple expressions") {
    val calculator = getCalculator
    assertResult(Right(3))(calculator("3"))
    assertResult(Right(-4.6))(calculator("1 -5.6"))
    assertResult(Right(-.1))(calculator("-.1"))
    assertResult(Right(3.2))(calculator("2.1+1.1"))
    assertResult(Right(30.62))(calculator("-.2 + .6 * (54 - 3 / 2 - .3) - .3 / .6"))
  }

  test("Calculator reports errors") {
    val calculator = getCalculator
    assert(calculator("2+@/3").isLeft)
    assert(calculator("1/(5-5)").isLeft)
    assert(calculator("2 + x").isLeft)
  }
}
