/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.cutpoints

import com.barrybecker4.math.Range
import org.scalatest.funsuite.AnyFunSuite



class TightCutPointFinderSuite extends AnyFunSuite {

  private val finder = new TightCutPointFinder

  test("FindCutPoints") {
    val cuts = finder.getCutPoints(Range(10.0, 22.0), 5)
    assertResult(Array[Double](10.0, 15.0, 20.0, 22.0)) { cuts }
  }

  test("FindCutPointsWhenHighPrecisionEndPoints") {
    val cuts = finder.getCutPoints(Range(11.234, 22.567), 4)
    assertResult(Array[Double](11.234, 15.0, 20.0, 22.567)) { cuts }
  }

  test("FindCutPointsCrowdedEnds") {
    val cuts = finder.getCutPoints(Range(10.1, 19.9), 5)
    assertResult(Array[Double](10.1, 12.0, 14.0, 16.0, 18.0, 19.9)) { cuts }
  }

  test("FindCutPointsMaxTicks2") {
    val cuts = finder.getCutPoints(Range(0.0, 100.0), 2)
    assertResult(Array[Double](0.0, 100.0)) { cuts }
  }

  test("ZeroExtentReturnsExpandedEndpoints") {
    val cuts = finder.getCutPoints(Range(5.0, 5.0), 5)
    assert(cuts.length == 2)
    assert(cuts(0) == 5.0)
    assert(cuts(1) == 5.0 + AbstractCutPointFinder.MIN_RANGE)
  }

  test("RejectsMaxTicksBelow2") {
    intercept[IllegalArgumentException] {
      finder.getCutPoints(Range(0.0, 10.0), 1)
    }
  }

  test("RejectsInfiniteRange") {
    intercept[IllegalArgumentException] {
      finder.getCutPoints(Range(0.0, Double.PositiveInfinity), 5)
    }
  }
}
