/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.cutpoints

import com.barrybecker4.math.Range
import org.scalatest.funsuite.AnyFunSuite



class LooseCutPointFinderSuite extends AnyFunSuite {

  private val finder = new LooseCutPointFinder

  test("FindCutPoints") {
    val cuts = finder.getCutPoints(Range(10.0, 22.0), 5)
    assertResult(Array[Double](10.0, 15.0, 20.0, 25.0)) { cuts }
  }

  test("FindCutPointsNegativeRange") {
    val cuts = finder.getCutPoints(Range(-50.0, -10.0), 5)
    assertResult(Array[Double](-50.0, -40.0, -30.0, -20.0, -10.0)) { cuts }
  }

  test("FindCutPointsCrossingZero") {
    val cuts = finder.getCutPoints(Range(-3.0, 7.0), 5)
    assertResult(Array[Double](-5.0, 0.0, 5.0, 10.0)) { cuts }
  }

  test("FindCutPointsMaxTicks2") {
    val cuts = finder.getCutPoints(Range(0.0, 100.0), 2)
    assertResult(Array[Double](0.0, 200.0)) { cuts }
  }

  test("RejectsMaxTicksBelow2") {
    intercept[IllegalArgumentException] {
      finder.getCutPoints(Range(0.0, 10.0), 0)
    }
  }
}
