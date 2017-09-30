package ru.spbau.mit.ldvsoft.scala.bot.bot

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import info.mukel.telegrambot4s.api.declarative.{Commands, ToCommand}
import info.mukel.telegrambot4s.api.{Polling, TelegramBot}
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import ru.spbau.mit.ldvsoft.scala.bot.messages._
import ru.spbau.mit.ldvsoft.scala.bot.storage.TZBotStorage.{GetUserTimezone, SetUserTimezone, UnsetUserTimezone}

import scala.concurrent.Future.successful
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

class TZBot(val token: String, val dbActor: ActorRef) extends TelegramBot with Polling with Commands {
  import TZBot._

  onCommand("start") { implicit message =>
    reply("Hello! I am a bot to play with timezones!")
  }

  implicit val timeout: Timeout = Timeout(1 second)

  onMessage { implicit message =>
    message.text.foreach { text =>
      if (!text.startsWith(ToCommand.CommandPrefix)) {
        val replyFuture = process(text, message.chat.id)
        replyFuture.onComplete {
          case Success(x) => reply(x.toString)
          case _ => Unit
        }
      }
    }
  }

  def process(str: String, chatId: Long): Future[UserResponse] = {
    Parser(str) match {
      case None => successful(ErrorResponse("I didn't understand you..."))
      case Some(message) => try {
        message match {

          case ConvertTimeMessageWithFromAndTo(timeString, from, to) =>
            successful(TimeResponse(convertTime(timeString, from, to)))
          case ConvertTimeMessageWithFrom(timeString, from) =>
            dbActor ? GetUserTimezone(chatId) awaitAndResponse[Option[DateTimeZone]] {
              case None => ErrorResponse("I don't know your timezone.")
              case Some(to) => TimeResponse(convertTime(timeString, from, to))
            }
          case ConvertTimeMessageWithTo(timeString, to) =>
            dbActor ? GetUserTimezone(chatId) awaitAndResponse[Option[DateTimeZone]] {
              case None => ErrorResponse("I don't know your timezone.")
              case Some(from) => TimeResponse(convertTime(timeString, from, to))
            }

          case SetTimezone(tz) =>
            dbActor ? SetUserTimezone(chatId, tz) awaitAndResponse[Unit] { _ =>
              TimezoneResponse(Some(tz))
            }
          case UnsetTimezone =>
            dbActor ? UnsetUserTimezone(chatId) awaitAndResponse[Unit] { _ =>
              TimezoneResponse(None)
            }

          case _ =>
            successful(ErrorResponse("I am not yet smart enough to answer this question."))

        }
      } catch {
        case e: Exception => successful(ErrorResponse("Something went wrong.", e))
      }
    }
  }
}

object TZBot {
  private val timeFormatter = DateTimeFormat.forPattern("HH:mm")

  private def convertTime(source: String, from: DateTimeZone, to: DateTimeZone): String = {
    val moment = timeFormatter.withZone(from).parseMillis(source)
    timeFormatter.withZone(to).print(moment)
  }

  private implicit class MyFuture[T](val f: Future[T]) {
    def awaitAndResponse[U <: T](
                                  func: U => UserResponse
                                )(
                                  implicit executor: ExecutionContext
    ): Future[UserResponse] = f.transform[UserResponse] { it: Try[T] => Success(
      it match {
        case Success(value) => func(value.asInstanceOf[U])
        case Failure(e) => ErrorResponse("Something is wrong with our database", e)
      })
    }
  }
}
