package ru.spbau.mit.ldvsoft.scala.bot.messages

import org.joda.time.{DateTimeZone, Instant}

trait UserMessage

case class ConvertTimeMessageWithFromAndTo(time: String, source: DateTimeZone, target: DateTimeZone) extends UserMessage

case class ConvertTimeMessageWithFrom(time: String, source: DateTimeZone) extends UserMessage

case class ConvertTimeMessageWithTo(time: String, target: DateTimeZone) extends UserMessage

case class SetTimezone(timezone: DateTimeZone) extends UserMessage

case object UnsetTimezone extends UserMessage
