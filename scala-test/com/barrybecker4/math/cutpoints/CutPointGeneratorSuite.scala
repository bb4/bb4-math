/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.cutpoints

import com.barrybecker4.math.Range
import org.scalatest.funsuite.AnyFunSuite

import java.text.DecimalFormat

/**
  * High-value coverage for CutPointGenerator API behavior that the label suites do not pin down.
  */
class CutPointGeneratorSuite extends AnyFunSuite {

  private def generator(tight: Boolean = true) =
    new CutPointGenerator(tight, new DecimalFormat("###,###.##"))

  test("useTightLabelingTracksSetter") {
    val g = generator(true)
    assert(g.useTightLabeling)
    g.setUseTightLabeling(false)
    assert(!g.useTightLabeling)
    assertResult(Array(10.0, 15.0, 20.0, 25.0)) {
      g.getCutPoints(Range(10.0, 22.0), 5)
    }
    g.setUseTightLabeling(true)
    assert(g.useTightLabeling)
    assertResult(Array(10.0, 15.0, 20.0, 22.0)) {
      g.getCutPoints(Range(10.0, 22.0), 5)
    }
  }

  test("getCutPointsDelegatesToFinder") {
    val tight = generator(true).getCutPoints(Range(10.0, 22.0), 5)
    val loose = generator(false).getCutPoints(Range(10.0, 22.0), 5)
    assertResult(Array(10.0, 15.0, 20.0, 22.0))(tight)
    assertResult(Array(10.0, 15.0, 20.0, 25.0))(loose)
  }

  test("getCutPointLabelsRejectsMaxTicksBelow2") {
    intercept[IllegalArgumentException] {
      generator().getCutPointLabels(Range(0.0, 10.0), 1)
    }
  }
}
