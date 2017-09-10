package mit.spbau.ru.ldvsoft.scala

import scala.util.parsing.combinator.Parsers
import scala.util.parsing.input.{NoPosition, Position, Reader}

case class ParserError(reason: String) extends CalculationError

class Parser(val calculationContext: CalculationContext) extends Parsers {
  override type Elem = Token

  private def identifier: Parser[String] = {
    accept("identifier", { case IDENTIFIER(id) => id })
  }

  private def literal: Parser[Literal] = {
    accept("literal number", { case LITERAL(value) => Literal(value) })
  }

  private def operator: Parser[Operator] = {
    accept(s"operator, one of ${calculationContext.operators.keys}", {
      case OPERATOR(name) if calculationContext.operators contains name =>
        calculationContext.operators.get(name).orNull
    })
  }

  private def openingBracket: Parser[OPENING_BRACKET.type] = {
    accept("opening bracket", { case it @ OPENING_BRACKET => it })
  }

  private def closingBracket: Parser[CLOSING_BRACKET.type] = {
    accept("closing bracket", { case it @ CLOSING_BRACKET => it })
  }

  private def primitive: Parser[AST] = {
    (openingBracket ~ expression ~ closingBracket) ^^ { case _ ~ e ~ _ => e } | literal
  }

  private def expression: Parser[AST] = {
    (primitive ~ rep1(operator ~ primitive)) ^^ { case first ~ listOfOps =>
      val list: Seq[Either[AST, Any with Operator]] = Left(first) ::
        listOfOps.flatMap { case b ~ a => Right(b) :: Left(a) :: Nil }
      buildAstFromOperators(list)
    } | primitive
  }

  private def buildAstFromOperators(input: Seq[Either[AST, Any with Operator]]): AST = {
    var astStack: List[AST] = List()
    var operatorStack: List[Operator] = List()

    def popOperatorStack(): Unit = {
      val op = operatorStack.head
      if (astStack.size < 2)
        throw new IllegalArgumentException(s"Not enough operands for operator $op")
      val right :: left :: remaining = astStack
      operatorStack = operatorStack.tail
      astStack = OperatorApplication(op, left, right) :: remaining
    }

    input.foreach({
      case Left(a) => astStack = a :: astStack
      case Right(op) =>
        var continuePopping = true
        while (operatorStack.nonEmpty && continuePopping) {
          val op1 = operatorStack.head
          var needToPop: Boolean = false
          if (op.isLeftAssociative)
            needToPop = op.priority <= op1.priority
          else
            needToPop = op.priority < op1.priority
          if (needToPop)
            popOperatorStack()
          else
            continuePopping = false
        }
        operatorStack = op :: operatorStack
    })
    while (operatorStack.nonEmpty)
      popOperatorStack()
    if (astStack.size != 1)
      throw new IllegalArgumentException("AST tree not folded into one root")
    astStack.head
  }

  def apply(tokens: Seq[Token]): Either[ParserError, AST] = {
    val reader = new TokenReader(tokens)
    expression(reader) match {
      case NoSuccess(msg, _) => Left(ParserError(msg))
      case Success(ast, _) => Right(ast)
    }
  }
}

class TokenReader(tokens: Seq[Token]) extends Reader[Token] {
  override def first: Token = tokens.head
  override def atEnd: Boolean = tokens.isEmpty
  override def pos: Position = NoPosition
  override def rest: Reader[Token] = new TokenReader(tokens.tail)
}


