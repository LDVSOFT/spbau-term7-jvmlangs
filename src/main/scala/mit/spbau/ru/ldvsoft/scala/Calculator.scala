package mit.spbau.ru.ldvsoft.scala

class Calculator(val calculationContext: CalculationContext) {
  def apply(input: String): Either[CalculationError, Double] = {
    val lexer = new Lexer()
    val parser = new Parser(calculationContext)
    for {
      lexemes <- lexer(input)
      parsed <- parser(lexemes)
      result <- parsed.calculate(calculationContext)
    } yield result
  }
}