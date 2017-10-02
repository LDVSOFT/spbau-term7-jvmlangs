package net.ldvsoft.spbau.scala

trait MutableMultiSet[T] extends ImmutableMultiSet[T] {
  def add(e: T)
  def remove(e: T)
  def removeCompletely(e: T)

  def addAll(es: Seq[T])
  def removeAll(es: Seq[T])
  def removeAllCompletely(es: Seq[T])
  def clear()
}

object MutableMultiSet {
  def apply[T](es: T*): MutableMultiSet[T] = MultiSetImpl[T](es)

  def unapplySeq[T](arg: MutableMultiSet[T]): Some[Seq[T]] = Some(arg.seq)
}