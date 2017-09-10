package mit.spbau.ru.ldvsoft.scala

import scala.util.parsing.combinator.RegexParsers

case class LexerError(reason: String) extends CalculationError

class Lexer extends RegexParsers {
  val identifier: Parser[IDENTIFIER] = {
    "[a-zA-Z_][a-zA-Z0-9_]*".r ^^ { name => IDENTIFIER(name) }
  }

  val literal: Parser[LITERAL] = {
    "-?[0-9]+(?:\\.[0-9]+)?".r ^^ { number => LITERAL(number.toDouble) }
  }

  val operator: Parser[OPERATOR] = {
    "[-+*/^]".r ^^ { op => OPERATOR(op) }
  }

  def openingBracket: Parser[OPENING_BRACKET.type] = "(" ^^ { _ => OPENING_BRACKET }
  def closingBracket: Parser[CLOSING_BRACKET.type] = ")" ^^ { _ => CLOSING_BRACKET }

  def tokens: Parser[List[Token]] = phrase(rep1(identifier | literal | operator | openingBracket | closingBracket))

  def apply(input: String): Either[LexerError, List[Token]] = {
    parse(tokens, input) match {
      case NoSuccess(msg, _) => Left(LexerError(msg))
      case Success(result, _) => Right(result)
    }
  }
}
