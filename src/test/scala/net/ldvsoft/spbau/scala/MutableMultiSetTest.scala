package net.ldvsoft.spbau.scala

import org.scalatest.FunSuite

class MutableMultiSetTest extends FunSuite {
  test("Test addition") {
    val set = MutableMultiSet[Int]()

    assertResult(Seq())(set.seq.sorted)

    set.add(1)
    set.add(2)
    set.add(3)
    set.add(2)

    assertResult(Seq(1, 2, 2, 3))(set.seq.sorted)

    set.add(1)

    assertResult(Seq(1, 1, 2, 2, 3))(set.seq.sorted)

    set.addAll(Seq(1, 2, 3, 4))

    assertResult(Seq(1, 1, 1, 2, 2, 2, 3, 3, 4))(set.seq.sorted)
  }

  test("Test removal") {
    val set = MutableMultiSet(1, 2, 2, 4, 5, 7, 7, 7, 9)

    set.remove(1)
    set.remove(2)
    set.remove(11)

    assertResult(Seq(2, 4, 5, 7, 7, 7, 9))(set.seq.sorted)

    set.removeAll(Seq(7, 7, 5))

    assertResult(Seq(2, 4, 7, 9))(set.seq.sorted)
  }

  test("Test complete removal") {
    val set = MutableMultiSet(1, 2, 2, 4, 5, 7, 7, 7, 9)

    set.removeCompletely(1)
    set.removeCompletely(2)
    set.removeCompletely(11)

    assertResult(Seq(4, 5, 7, 7, 7, 9))(set.seq.sorted)

    set.removeAllCompletely(Seq(7, 7, 5))

    assertResult(Seq(4, 9))(set.seq.sorted)
  }

  test("Test clear") {
    val set = MutableMultiSet(1, 2, 2, 4, 5, 7, 7, 7, 9)
    set.clear()
    assertResult(true)(set.isEmpty)
    assertResult(Seq())(set.seq)
  }
}
