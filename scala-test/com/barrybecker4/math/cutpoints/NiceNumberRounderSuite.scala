/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.cutpoints

import com.barrybecker4.math.MathUtil
import com.barrybecker4.math.cutpoints.NiceNumberRounderSuite._
import org.scalactic.{Equality, TolerantNumerics}
import org.scalatest.funsuite.AnyFunSuite


object NiceNumberRounderSuite {
  private val BASE_VALUE = 101.34
  private val EXPECTED_ROUNDED_VALUES = Array(
    100.0, 100.0, 200.0, 200.0, 200.0, 200.0, 200.0, 500.0, 500.0, 500.0, 500.0, 500.0, 500.0, 500.0, 500.0, 500.0,
    500.0, 500.0, 500.0, 500.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0,
    1000.0, 1000.0, 1000.0, 1000.0, 1000.0)
  private val EXPECTED_CEILED_VALUES = Array(
    200.0, 200.0, 200.0, 200.0, 500.0, 500.0, 500.0, 500.0, 500.0, 500.0, 500.0, 500.0, 500.0, 500.0,
    1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0,
    1000.0, 1000.0, 1000.0, 2000.0, 2000.0, 2000.0, 2000.0)
}


class NiceNumberRounderSuite extends AnyFunSuite {

  /** Rounder uses Math.log10/pow; results may differ in the last bit across JVMs. */
  implicit val doubleEq: Equality[Double] = TolerantNumerics.tolerantDoubleEquality(MathUtil.EPS_MEDIUM)

  test("RoundNumberSmall") {
    assert(0.001 === Rounder.roundDown(0.001234567))
  }

  test("RoundNumberMediumUpper") {
    assert(10.0 === Rounder.roundDown(8.87653))
  }

  test("RoundNumberMediumLower") {
    assert(5.0 === Rounder.roundDown(4.363))
  }

  test("RoundNumberLargeUpper") {
    assert(200000000000.0 === Rounder.roundDown(172034506708.90123))
  }

  test("RoundNumberLargeLower") {
    assert(200000000000.0 === Rounder.roundDown(172034506708.90123))
  }

  test("RoundNumber") {
    var index = 0
    for (inc <- 0 until 1000 by 30) {
      val value = BASE_VALUE + inc
      assert(EXPECTED_ROUNDED_VALUES(index) === Rounder.roundDown(value))
      index += 1
    }
    assert(200000000000.0 === Rounder.roundDown(172034506708.90123))
  }

  test("CeilNumberSmall") {
    assert(0.002 === Rounder.roundUp(0.001234567))
  }

  test("CeilNumberMediumUpper") {
    assert(10.0 === Rounder.roundUp(8.87653))
  }

  test("CeilNumberMediumLower") {
    assert(5.0 === Rounder.roundUp(4.363))
  }

  test("CeilNumberLargeUpper") {
    assert(200000000000.0 === Rounder.roundUp(172034506708.90123))
  }

  test("CeilNumberLargeLower") {
    assert(200000000000.0 === Rounder.roundUp(102034506708.90123))
  }

  test("CeiledNumber") {
    var index = 0
    for (inc <- 0 until 1000 by 30) {
      val value = BASE_VALUE + inc
      assert(EXPECTED_CEILED_VALUES(index) === Rounder.roundUp(value))
      index += 1
    }
  }
}
