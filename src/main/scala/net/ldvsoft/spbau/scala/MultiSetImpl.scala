package net.ldvsoft.spbau.scala

import scala.collection.mutable

final class MultiSetImpl[T] private(val storage: mutable.Map[Any, Int] = mutable.Map.empty) extends MutableMultiSet[T] {
  override def seq: Seq[T] = storage.flatMap({
    case (e, x) => (1 to x) map (_ => e.asInstanceOf[T])
  }).toSeq

  override def add(e: T): Unit = storage(e) = storage.getOrElse(e, 0) + 1

  override def remove(e: T): Unit = {
    if (!apply(e))
      return
    storage(e) -= 1
  }

  override def removeCompletely(e: T): Unit = storage.remove(e)

  override def addAll(es: Seq[T]): Unit = es foreach add

  override def removeAll(es: Seq[T]): Unit = es foreach remove

  override def removeAllCompletely(es: Seq[T]): Unit = es foreach removeCompletely

  override def clear(): Unit = storage.clear()

  override def size: Int = storage.values.sum

  override def isEmpty: Boolean = size == 0

  override def apply(e: Any): Boolean = storage.getOrElse(e, 0) > 0

  override def count(e: Any): Int = storage.getOrElse(e, 0)

  override def containsAll(es: Seq[Any]): Boolean = {
    val against = MultiSetImpl(es)
    against.storage map {
      case (key, count) => storage.getOrElse(key, 0) >= count
    } forall identity
  }

  override def find(predicate: T => Boolean): Option[T] = storage
    .filter({ case (_, n) => n > 0 })
    .keys
    .map(it => it.asInstanceOf[T])
    .find(predicate)
}

object MultiSetImpl {
  def apply[T](seq: Seq[T]): MultiSetImpl[T] = {
    new MultiSetImpl[T](mutable.Map(seq.groupBy(identity).mapValues(it => it.length).toSeq:_*))
  }
}