package net.ldvsoft.spbau.scala

import org.scalatest.FunSuite

class ImmutableMultiSetTest extends FunSuite {
  val emptySet = ImmutableMultiSet()
  val set = ImmutableMultiSet(1, 2, 3, 4, 4, 5)

  test("Test size and emptiness") {
    assertResult(true)(emptySet.isEmpty)
    assertResult(false)(set.isEmpty)

    assertResult(0)(emptySet.size)
    assertResult(6)(set.size)
  }

  test("Test containment and search") {
    assertResult(false)(set(0))
    assertResult(true)(set(2))
    assertResult(true)(set(4))

    assertResult(0)(set.count(0))
    assertResult(1)(set.count(2))
    assertResult(2)(set.count(4))

    assertResult(None)(set.find(_ == 6))
    assertResult(Some(4))(set.find(_ == 4))
    assertResult(Some(3))(set.find(_ == 3))
  }

  test("Test seq") {
    assertResult(Seq())(emptySet.seq)
    assertResult(Seq(1, 2, 3, 4, 4, 5))(set.seq.sorted)
  }

  test("Test mass containment") {
    assertResult(true)(emptySet.containsAll(Seq()))
    assertResult(true)(set.containsAll(Seq()))

    assertResult(false)(emptySet.containsAll(Seq(1)))
    assertResult(true)(set.containsAll(Seq(1)))
    assertResult(true)(set.containsAll(Seq(1, 2)))
    assertResult(false)(set.containsAll(Seq(2, 7)))
    assertResult(false)(set.containsAll(Seq(4, 4, 4)))
  }

  test("Test filter") {
    assertResult(Seq(2, 4, 4))(set.filter(_ % 2 == 0).seq.sorted)
  }

  test("Test map and flatMap") {
    assertResult(Seq(1, 4, 9, 16, 16, 25))((set map (x => x * x)).seq.sorted)
    assertResult(Seq(2, 4, 4, 4, 16, 16))(set.flatMap({
      case x if x % 2 == 0 => Seq(x * x, x)
      case _ => Seq()
    }).seq.sorted)
  }

  test("Test addition and subtraction") {
    val a = ImmutableMultiSet(1, 2, 2, 3)
    val b = ImmutableMultiSet(1, 3, 3, 4)
    assertResult(Seq(1, 1, 2, 2, 3, 3, 3, 4))((a + b).seq.sorted)
    assertResult(Seq(3, 4))((b - a).seq.sorted)
  }

  test("Test unapplySeq") {
    set match {
      case ImmutableMultiSet(a, b, c, d, e, f) =>
        assertResult(set.seq)(Seq(a, b, c, d, e, f))
      case _ => fail("Not matched")
    }
  }
}
