package net.ldvsoft.spbau.scala

trait ImmutableMultiSet[+T] {
  def size: Int
  def isEmpty: Boolean

  def apply(e: Any): Boolean
  def count(e: Any): Int
  def containsAll(es: Seq[Any]): Boolean

  def find(predicate: T => Boolean): Option[T]

  def seq: Seq[T]

  def filter(predicate: T => Boolean): ImmutableMultiSet[T] = ImmutableMultiSet(seq.filter(predicate):_*)

  def map[R](f: T => R): ImmutableMultiSet[R] = ImmutableMultiSet(seq.map(f):_*)

  def flatMap[R](f: T => Seq[R]): ImmutableMultiSet[R] = ImmutableMultiSet(seq.flatMap(f):_*)

  def +[U >: T](b: ImmutableMultiSet[U]): ImmutableMultiSet[U] = {
    val t = MutableMultiSet[U](seq:_*)
    t.addAll(b.seq)
    t
  }

  def -[U >: T](b: ImmutableMultiSet[U]): ImmutableMultiSet[U] = {
    val t = MutableMultiSet[U](seq:_*)
    t.removeAll(b.seq)
    t
  }
}

object ImmutableMultiSet {
  def apply[T](es: T*): ImmutableMultiSet[T] = MultiSetImpl[T](es)

  def unapplySeq[T](arg: ImmutableMultiSet[T]): Some[Seq[T]] = Some(arg.seq)
}