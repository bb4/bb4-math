/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.complex

import com.barrybecker4.math.MathUtil
import org.scalactic.{Equality, TolerantNumerics}
import org.scalatest.funsuite.AnyFunSuite


/**
  * Characterization tests for [[ComplexNumberRange]].
  */
class ComplexNumberRangeSuite extends AnyFunSuite {

  implicit val doubleEq: Equality[Double] = TolerantNumerics.tolerantDoubleEquality(MathUtil.EPS_MEDIUM)

  private val range = ComplexNumberRange(
    ComplexNumber(-2.0, -1.0),
    ComplexNumber(2.0, 1.0)
  )

  test("toString") {
    assertResult("-2.0 - 1.0i to 2.0 + 1.0i") { range.toString }
  }

  test("interpolate at corners") {
    assertResult(ComplexNumber(-2.0, -1.0)) {
      range.getInterpolatedPosition(0.0, 0.0)
    }
    assertResult(ComplexNumber(2.0, 1.0)) {
      range.getInterpolatedPosition(1.0, 1.0)
    }
    assertResult(ComplexNumber(2.0, -1.0)) {
      range.getInterpolatedPosition(1.0, 0.0)
    }
    assertResult(ComplexNumber(-2.0, 1.0)) {
      range.getInterpolatedPosition(0.0, 1.0)
    }
  }

  test("interpolate at center") {
    val mid = range.getInterpolatedPosition(0.5, 0.5)
    assert(0.0 === mid.real)
    assert(0.0 === mid.imaginary)
  }

  test("interpolate outside unit square extrapolates") {
    val outside = range.getInterpolatedPosition(1.5, -0.5)
    assert(4.0 === outside.real)
    assert(-2.0 === outside.imaginary)
  }
}
