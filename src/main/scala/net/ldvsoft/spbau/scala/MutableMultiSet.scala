package net.ldvsoft.spbau.scala

trait MutableMultiSet[T] extends ImmutableMultiSet[T] {
  def add(e: T)
  def remove(e: T)
  def removeCompletely(e: T)

  def addAll(es: Seq[T])
  def removeAllCompletely(es: Seq[T])
  def clear()
}
