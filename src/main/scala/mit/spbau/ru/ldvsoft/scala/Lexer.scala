package mit.spbau.ru.ldvsoft.scala

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex

case class LexerError(reason: String) extends CalculationError

class Lexer {
  val literalRegex: Regex = """-?(?:[0-9]*\.[0-9]+|[0-9]+)""".r
  val identifierRegex: Regex = """[a-zA-Z][a-zA-Z0-9_]*""".r
  val infixOperatorRegex: Regex = "[-+*/^]".r

  private def tryMatchLiteral(currentSuffix: String): Option[(LiteralToken, Int)] = {
    literalRegex.findPrefixOf(currentSuffix).map(it => (LiteralToken(it.toDouble), it.length))
  }

  private def tryMatchIdentifier(currentSuffix: String): Option[(IdentifierToken, Int)] = {
    identifierRegex.findPrefixOf(currentSuffix).map(it => (IdentifierToken(it), it.length))
  }

  private def tryMatchInfixOperator(expectingInfix: Boolean, currentSuffix: String): Option[(InfixOperatorToken, Int)] = {
    if (!expectingInfix)
      None
    else
      infixOperatorRegex.findPrefixOf(currentSuffix).map(it => (InfixOperatorToken(it), it.length))
  }

  private def tryMatchBracket(currentSuffix: String): Option[(Token, Int)] = {
    "[()]".r.findPrefixOf(currentSuffix).map({
      case "(" => (OpeningBracketToken, 1)
      case ")" => (ClosingBracketToken, 1)
    })
  }

  private def shouldExpectInfixOperator(lastToken: Token): Boolean = lastToken match {
    case ClosingBracketToken => true
    case LiteralToken(_) => true
    case IdentifierToken(_) => true
    case _ => false
  }

  def apply(input: String): Either[LexerError, List[Token]] = {
    var result = ListBuffer[Token]()
    var position = 0
    var expectingInfixOperator = false
    while (position < input.length) {
      while (position < input.length && input(position).isSpaceChar)
        position += 1
      if (position < input.length) {
        val currentSuffix = input.substring(position, input.length)
        val (newLexeme, advance) = tryMatchInfixOperator(expectingInfixOperator, currentSuffix)
          .orElse(tryMatchLiteral(currentSuffix))
          .orElse(tryMatchIdentifier(currentSuffix))
          .orElse(tryMatchBracket(currentSuffix))
          .getOrElse(return Left(LexerError(s"Unknown lexeme at ${position + 1}")))
        position += advance
        result += newLexeme
        expectingInfixOperator = shouldExpectInfixOperator(newLexeme)
      }
    }
    Right(result.toList)
  }
}
