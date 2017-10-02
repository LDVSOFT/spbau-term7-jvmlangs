package ru.spbau.mit.ldvsoft.scala.bot

import akka.actor.{ActorSystem, Props}
import ru.spbau.mit.ldvsoft.scala.bot.bot.TZBot
import ru.spbau.mit.ldvsoft.scala.bot.storage.TZBotStorage

object Main extends App {
  private val token = System.getenv("TOKEN")

  private val system = ActorSystem()

  private val dbActor = system.actorOf(Props[TZBotStorage])

  private val bot = new TZBot(token, dbActor)

  bot.run()
}