package ru.spbau.mit.ldvsoft.scala.bot.storage

import akka.persistence.PersistentActor
import org.joda.time.DateTimeZone

import scala.collection.mutable

class TZBotStorage extends PersistentActor {
  import TZBotStorage._

  private val map: mutable.HashMap[User, DateTimeZone] = mutable.HashMap.empty

  private def receiveEvent: Event => Unit = {
    case SetUserTimezone(user, value) => map(user) = value
    case UnsetUserTimezone(user) => map.remove(user)
  }

  override def receiveRecover: Receive = {
    case e: Event => receiveEvent(e)
  }

  override def receiveCommand: Receive = {
    case e: Event =>
      sender ! receiveEvent(e)
    case GetUserTimezone(user) =>
      sender ! map.get(user)
  }

  override def persistenceId = "tzbot-storage"
}

object TZBotStorage {
  type User = Long

  sealed trait Message

  sealed trait Event extends Message

  sealed trait Query extends Message

  case class SetUserTimezone(user: User, value: DateTimeZone) extends Event

  case class UnsetUserTimezone(user: User) extends Event

  case class GetUserTimezone(user: User) extends Query
}
