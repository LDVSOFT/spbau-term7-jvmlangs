package mit.spbau.ru.ldvsoft.scala

object Main {
  def main(args: Array[String]): Unit = {
    val builder = new Calculator.Builder()
    DefaultOperators.addDefaultOperators(builder)
    val calculator = builder.build()

    while (true) {
      val input  = scala.io.StdIn.readLine("Enter expression: ")
      if (input == null || input == "")
        return
      calculator(input) match {
        case Right(value) => println(s"= $value")
        case Left(error) => println(s"Error ${error.getClass.getName}: ${error.reason}")
      }
    }
  }
}