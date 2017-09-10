package mit.spbau.ru.ldvsoft.scala

sealed trait Token

case class LITERAL(value: Double) extends Token
case class IDENTIFIER(name: String) extends Token
case class OPERATOR(op: String) extends Token
case object OPENING_BRACKET extends Token
case object CLOSING_BRACKET extends Token
