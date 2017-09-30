package ru.spbau.mit.ldvsoft.scala.bot.messages

import org.joda.time.DateTimeZone

import scala.language.postfixOps
import scala.util.parsing.combinator.RegexParsers

object Parser extends RegexParsers {
  private def timezone: Parser[DateTimeZone] = {
    ("(?i)UTC".r?) ~> ("[+-][0-9]{1,2}".r ~ opt(":" ~> "[0-9]{2}".r)) ^^ {
      case h ~ mOpt =>
        val m = mOpt.getOrElse("00")
        val formatted = "%+03d:%02d".format(h.toInt, m.toInt)
        DateTimeZone.forID(formatted)
    } |
    "(?i)UTC".r ^^^ DateTimeZone.forID("+00:00")
  }

  private def time: Parser[String] = {
    "[0-9]+".r ~ opt(opt(":") ~> "[0-9]+".r) ^^ {
      case hours ~ minutesOpt =>
        val minutes: String = minutesOpt.getOrElse("00")
        s"$hours:$minutes"
    }
  }

  private def convertTimeMessageFromTo: Parser[ConvertTimeMessageWithFromAndTo] = {
    time ~ "(?i)from".r ~ timezone ~ "(?i)to".r ~ timezone ^^ {
      case time ~ _ ~ from ~ _ ~ to => ConvertTimeMessageWithFromAndTo(time, from, to)
    }
  }

  private def convertTimeMessageFrom: Parser[ConvertTimeMessageWithFrom] = {
    time ~ "(?i)from".r ~ timezone ^^ {
      case time ~ _ ~ from  => ConvertTimeMessageWithFrom(time, from)
    }
  }

  private def convertTimeMessageTo: Parser[ConvertTimeMessageWithTo] = {
    time ~ "(?i)to".r ~ timezone ^^ {
      case time ~ _ ~ to  => ConvertTimeMessageWithTo(time, to)
    }
  }

  private def setTimezone: Parser[SetTimezone] = {
    "(?i)set".r ~> "(?i)timezone".r ~> timezone ^^ (tz => SetTimezone(tz))
  }

  private def unsetTimezone: Parser[UnsetTimezone.type] = {
    "(?i)unset".r ~ "(?i)timezone".r ^^^ UnsetTimezone
  }

  private def message: Parser[UserMessage] = phrase(
    convertTimeMessageFromTo |
    convertTimeMessageFrom |
    convertTimeMessageTo |
    setTimezone |
    unsetTimezone
  )

  def apply(text: String): Option[UserMessage] = {
    parse(message, text) match {
      case Success(userMessage, _) => Some(userMessage)
      case _ => None
    }
  }
}
