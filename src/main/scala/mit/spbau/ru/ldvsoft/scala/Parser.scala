package mit.spbau.ru.ldvsoft.scala

import scala.util.parsing.combinator.Parsers
import scala.util.parsing.input.{NoPosition, Position, Reader}

case class ParserError(reason: String) extends CalculationError

class Parser(val calculationContext: CalculationContext) extends Parsers {
  override type Elem = Token

  private def literal: Parser[Literal] = {
    accept("literal number", { case LiteralToken(value) => Literal(value) })
  }

  private def infixOperator: Parser[InfixOperator] = {
    accept(s"infix operator, one of ${calculationContext.infixOperators.keys}", {
      case InfixOperatorToken(name) if calculationContext.infixOperators contains name =>
        calculationContext.infixOperators.get(name).orNull
    })
  }

  private def openingBracket: Parser[OpeningBracketToken.type] = {
    accept("opening bracket", { case it @ OpeningBracketToken => it })
  }

  private def closingBracket: Parser[ClosingBracketToken.type] = {
    accept("closing bracket", { case it @ ClosingBracketToken => it })
  }

  private def primitive: Parser[AST] = (openingBracket ~> expression <~ closingBracket) | literal

  private def expression: Parser[AST] = {
    (primitive ~ rep(infixOperator ~ primitive)) ^^ { case first ~ listOfOps =>
      val list: Seq[Either[AST, InfixOperator]] = Left(first) ::
        listOfOps.flatMap { case b ~ a => Right(b) :: Left(a) :: Nil }
      buildAstFromOperators(list)
    }
  }

  private def fullExpression: Parser[AST] = phrase(expression)

  private def buildAstFromOperators(input: Seq[Either[AST, InfixOperator]]): AST = {
    var astStack: List[AST] = List()
    var operatorStack: List[InfixOperator] = List()

    def popOperatorStack(): Unit = {
      val op = operatorStack.head
      if (astStack.size < 2)
        throw new IllegalArgumentException(s"Not enough operands for infixOperator $op")
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
    fullExpression(reader) match {
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


