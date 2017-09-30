package ru.spbau.mit.ldvsoft.scala.bot.messages

import org.joda.time.DateTimeZone
import org.scalatest.FunSuite

class ParserTest extends FunSuite {
  test("Parsing ConvertTimeMessageWithFromAndTo or without") {
    assertResult(
      Some(ConvertTimeMessageWithFromAndTo("8:10", DateTimeZone.forID("+03"), DateTimeZone.UTC))
    )(
      Parser("8:10 from UTC+03:00 to UTC")
    )
    assertResult(
      Some(ConvertTimeMessageWithFromAndTo("12:00", DateTimeZone.UTC, DateTimeZone.forID("-05")))
    )(
      Parser("12 From Utc To -5")
    )

    assertResult(
      Some(ConvertTimeMessageWithTo("13:00", DateTimeZone.forID("+05")))
    )(
      Parser("13 To +5")
    )
    assertResult(
      Some(ConvertTimeMessageWithFrom("15:12", DateTimeZone.UTC))
    )(
      Parser("15:12 From uTc")
    )
  }

  test("Parsing Set/UnsetTimezone") {
    assertResult(
      Some(SetTimezone(DateTimeZone.UTC))
    )(
      Parser("Set timezone UTC")
    )
    assertResult(
      Some(SetTimezone(DateTimeZone.forID("+04")))
    )(
      Parser("set Timezone +04")
    )

    assertResult(
      Some(UnsetTimezone)
    )(
      Parser("Unset timezone")
    )
  }
}