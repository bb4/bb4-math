/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.interpolation

import org.scalatest.funsuite.AnyFunSuite


/**
  * Characterization tests for [[InterpolationMethod]] factory cases and package-level aliases.
  * @author Barry Becker
  */
class InterpolationMethodSuite extends AnyFunSuite {

  private val sample = Array(0.0, 1.0, 2.0)

  test("VALUES order and size") {
    assertResult(5) { InterpolationMethod.VALUES.length }
    assertResult(Array(STEP, LINEAR, CUBIC, COSINE, HERMITE)) {
      InterpolationMethod.VALUES
    }
  }

  test("createInterpolator returns expected concrete types") {
    assert(STEP.createInterpolator(sample).isInstanceOf[StepInterpolator])
    assert(LINEAR.createInterpolator(sample).isInstanceOf[LinearInterpolator])
    assert(CUBIC.createInterpolator(sample).isInstanceOf[CubicInterpolator])
    assert(COSINE.createInterpolator(sample).isInstanceOf[CosineInterpolator])
    assert(HERMITE.createInterpolator(sample).isInstanceOf[HermiteInterpolator])
  }

  test("LINEAR factory matches LinearInterpolator at midpoint") {
    val viaFactory = LINEAR.createInterpolator(sample)
    val direct = new LinearInterpolator(sample)
    assertResult(direct.interpolate(0.5)) { viaFactory.interpolate(0.5) }
  }
}
