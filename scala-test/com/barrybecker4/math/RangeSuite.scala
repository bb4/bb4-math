/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math

import org.scalactic.{Equality, TolerantNumerics}
import scala.compiletime.uninitialized
import org.scalatest.funsuite.AnyFunSuite



object RangeSuite {
  val TOL = 0.000000000001
}

class RangeSuite extends AnyFunSuite {
  /** instance under test */
  private var range: Range = uninitialized

  val epsilon = 1e-6f
  implicit val doubleEq: Equality[Double] = TolerantNumerics.tolerantDoubleEquality(RangeSuite.TOL)


  test("DefaultConstruction") {
    range = Range()
    assert(range.getExtent.isNaN)
  }

  test("Add 2 to default and get 0 ragne") {
    range = Range()
    range = range.add(2)
    assert(range.getExtent == 0)
  }

  test("TypicalConstruction") {
    range = Range(1.2, 3.4)
    assert(2.2 === range.getExtent)
    assert(1.2 === range.min)
    assert(3.4 === range.max)
  }

  test("InvalidConstruction") {
    assertThrows[AssertionError] {
      Range(5.2, 3.4)
    }
  }

  test("ExtendRangeHigher") {
    range = Range(2, 3)
    range = range.add(5)
    assert(5.0 === range.max)
  }

  test("ExtendRangeLower") {
    range = Range(2, 3)
    range = range.add(-2)
    assert(-2.0 === range.min)
  }

  test("ExtendByRange") {
    range = Range(2, 3)
    val range2 = Range(-1.5, 2.5)
    range = range.add(range2)
    assert(-1.5 === range.min)
    assert(3.0 === range.max)
  }

  test("MapToUnitScale typical") {
    range = Range(2, 6)
    assert(0.0 === range.mapToUnitScale(2))
    assert(1.0 === range.mapToUnitScale(6))
    assert(0.5 === range.mapToUnitScale(4))
  }

  test("MapToUnitScale zero extent") {
    range = Range(5, 5)
    assert(0.0 === range.mapToUnitScale(5))
    assert(0.0 === range.mapToUnitScale(99))
  }

  test("MapToUnitScale empty NaN range") {
    range = Range()
    assert(range.mapToUnitScale(0).isNaN)
    assert(range.mapToUnitScale(1).isNaN)
  }
}
