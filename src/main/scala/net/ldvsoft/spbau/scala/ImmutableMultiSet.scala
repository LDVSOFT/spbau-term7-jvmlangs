package net.ldvsoft.spbau.scala

import java.util

import scala.collection.immutable.Set

trait ImmutableMultiSet[+T] {
  def size: Int
  def isEmpty: Boolean

  def apply(e: Any): Boolean
  def count(e: Any): Int
  def containsAll(es: Seq[Any])

  def find(predicate: T => Boolean): Option[T]

  def seq: Seq[T]

  def filter(predicate: T => Boolean): ImmutableMultiSet[T] = ImmutableMultiSet(seq.filter(predicate):_*)

  def map[R](f: T => R): ImmutableMultiSet[R] = ImmutableMultiSet(seq.map(f):_*)

  def flatMap[R](f: T => Seq[R]): ImmutableMultiSet[R] = ImmutableMultiSet(seq.flatMap(f):_*)

  def +(b: ImmutableMultiSet[T]): ImmutableMultiSet[T] = {
    var keys: 
  }
}

object ImmutableMultiSet {
  def apply[T](es: T*): ImmutableMultiSet[T] = new MultiSetImpl[T](es)

  def unapplySeq[T](arg: ImmutableMultiSet[T]): Some[Seq[T]] = Some(arg.seq)
}

object Test {
  def main(args: Array[String]): Unit = {
    val x = ImmutableMultiSet(1, 2, 2, 3, 5, 6, 7, 78)
    println(x)
    println(x.getClass)
    x match {
//      case Set(a, b, c, _, _, _, _) => println(a, b, c)
      case _ => println("nope")
    }
  }
}