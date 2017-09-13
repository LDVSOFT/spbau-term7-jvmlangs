package mit.spbau.ru.ldvsoft.scala

import org.scalatest.FunSuite

class LexerTest extends FunSuite{
  test("Primitives must be properly read") {
    val lexer = new Lexer()
    assertResult(Right(List(LiteralToken(1))))(lexer(" 1"))
    assertResult(Right(List(LiteralToken(1.2))))(lexer("1.2"))
    assertResult(Right(List(LiteralToken(.2))))(lexer(".2"))
    assertResult(Right(List(LiteralToken(-22.3))))(lexer("-22.3"))

    assertResult(Right(List(LiteralToken(0), InfixOperatorToken("+"))))(lexer("  0+"))
    assertResult(Right(List(LiteralToken(0), InfixOperatorToken("-"), LiteralToken(1))))(lexer("0-1"))

    assertResult(Right(List(OpeningBracketToken)))(lexer("("))
    assertResult(Right(List(ClosingBracketToken)))(lexer(") "))
    assertResult(Right(List(IdentifierToken("f123"))))(lexer("f123"))
  }

  test("Combined primitives must be properly read") {
    val lexer = new Lexer()
    assertResult(Right(List(LiteralToken(1.0), InfixOperatorToken("+"), LiteralToken(2.0))))(lexer(" 1 +2"))
    assertResult(Right(List(LiteralToken(4.1), InfixOperatorToken("-"), LiteralToken(2.2))))(lexer("4.1 - 2.2"))
    assertResult(Right(List(LiteralToken(4.1), InfixOperatorToken("-"), LiteralToken(2.2))))(lexer("4.1 -2.2"))
  }

  test("Lexer fails on unknown tokens") {
    val lexer = new Lexer()
    assert(lexer("1*@+2").isLeft)
    assert(lexer("1&222").isLeft)
  }
}
