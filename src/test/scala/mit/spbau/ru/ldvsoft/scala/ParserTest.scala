package mit.spbau.ru.ldvsoft.scala

import mit.spbau.ru.ldvsoft.scala.DefaultOperators.{Addition, Division, Multiplication, Subtraction}
import org.scalatest.FunSuite

class ParserTest extends FunSuite {
  private implicit def wrapToLiteral(double: Double): Literal = Literal(double)
  private implicit def wrapToToken(double: Double): LiteralToken = LiteralToken(double)

  private def getParser = {
    val builder = new CalculationContext.Builder()
    DefaultOperators.addDefaultOperators(builder)
    val context = builder.build()
    new Parser(context)
  }

  test("Parser properly parses") {
    val parser = getParser
    assertResult(Right(Literal(1.0)))(parser(List(1.0)))
    assertResult(Right(OperatorApplication(Addition, 1.2, 2.3)))(parser(List(1.2, InfixOperatorToken("+"), 2.3)))
  }

  test("Parser and lexer correctly parse expressions") {
    val lexer = new Lexer()
    val parser = getParser

    def tryParse(input: String): Either[CalculationError, AST] = {
      for {
        lexemes <- lexer(input)
        parsed <- parser(lexemes)
      } yield parsed
    }

    assertResult(Right(OperatorApplication(Addition, 1.0, 2.0)))(
      tryParse("1 + 2")
    )
    assertResult(Right(OperatorApplication(Subtraction, OperatorApplication(Division, 3.1, .5), .7)))(
      tryParse("3.1 / .5 - .7")
    )
    assertResult(Right(
      OperatorApplication(Subtraction,
        OperatorApplication(Addition,
            .2,
            OperatorApplication(Division,
              OperatorApplication(Multiplication, .3, .4),
              OperatorApplication(Subtraction, .5, .6))),
        .7
      )))(
      tryParse(".2+.3*.4/(.5-.6)-.7")
    )
  }
}
