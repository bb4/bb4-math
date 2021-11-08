/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.cutpoints

import com.barrybecker4.math.Range
import org.scalatest.funsuite.AnyFunSuite


/**
  * @author Barry Becker
  */
class LooseCutPointFinderSuite extends AnyFunSuite {

  private val finder = new LooseCutPointFinder

  test("FindCutPoints") {
    val cuts = finder.getCutPoints(Range(10.0, 22.0), 5)
    assertResult(Array[Double](10.0, 15.0, 20.0, 25.0)) { cuts }
  }
}
