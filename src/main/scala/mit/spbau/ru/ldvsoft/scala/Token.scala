package mit.spbau.ru.ldvsoft.scala

sealed trait Token

case class LiteralToken(value: Double) extends Token
case class IdentifierToken(name: String) extends Token
case class InfixOperatorToken(op: String) extends Token
case object OpeningBracketToken extends Token
case object ClosingBracketToken extends Token
