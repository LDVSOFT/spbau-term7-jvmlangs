package ru.spbau.mit.ldvsoft.scala.bot.messages

import org.joda.time.DateTimeZone

sealed trait UserResponse

case class ErrorResponse(reason: String, e: Option[Throwable] = None) extends UserResponse {
  override def toString: String = {
    val builder = new StringBuilder()
    builder ++= s"I failed to do the work: $reason"
    if (e.isDefined)
      builder ++= s"\n${e.get.getMessage}"
    builder.toString()
  }
}

object ErrorResponse {
  def apply(reason: String, e: Throwable): ErrorResponse = ErrorResponse(reason, Some(e))
}

case class TimeResponse(timeRepr: String) extends UserResponse {
  override def toString: String = s"Here is your time: $timeRepr"
}

case class TimezoneResponse(timezone: Option[DateTimeZone]) extends UserResponse {
  override def toString: String = timezone match {
    case Some(tz) => s"Your timezone is set to $tz."
    case None => s"Your timezone is not set."
  }
}