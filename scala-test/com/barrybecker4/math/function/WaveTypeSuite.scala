/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.function

import com.barrybecker4.math.MathUtil
import org.scalatest.funsuite.AnyFunSuite


/**
  * Characterization tests for [[WaveType]] (including package-level case names used by consumers).
  */
class WaveTypeSuite extends AnyFunSuite {

  test("VALUES order and size") {
    assertResult(4) { WaveType.VALUES.length }
    assertResult(Array(SINE_WAVE, SQUARE_WAVE, SAWTOOTH_WAVE, TRIANGLE_WAVE3)) {
      WaveType.VALUES
    }
  }

  test("display names") {
    assertResult("Sine Wave") { SINE_WAVE.toString }
    assertResult("Square Wave") { SQUARE_WAVE.toString }
    assertResult("Sawtooth Wave") { SAWTOOTH_WAVE.toString }
    assertResult("Triangle Wave") { TRIANGLE_WAVE3.toString }
  }

  test("sine wave offset uses Math.sin") {
    assert(Math.abs(SINE_WAVE.calculateOffset(0.0) - 0.0) < MathUtil.EPS_MEDIUM)
    assert(Math.abs(SINE_WAVE.calculateOffset(Math.PI / 2) - 1.0) < MathUtil.EPS_MEDIUM)
    assert(Math.abs(SINE_WAVE.calculateOffset(Math.PI) - 0.0) < MathUtil.EPS_MEDIUM)
    assert(Math.abs(SINE_WAVE.calculateOffset(3 * Math.PI / 2) - -1.0) < MathUtil.EPS_MEDIUM)
  }

  test("square wave is +1 or -1 by sign of sine") {
    assertResult(1.0) { SQUARE_WAVE.calculateOffset(0.1) }
    assertResult(-1.0) { SQUARE_WAVE.calculateOffset(Math.PI + 0.1) }
    // sin(0) == 0 is not > 0, so square wave is -1 at multiples of PI
    assertResult(-1.0) { SQUARE_WAVE.calculateOffset(0.0) }
  }

  test("sawtooth wave at known points") {
    assert(Math.abs(SAWTOOTH_WAVE.calculateOffset(0.0) - 0.0) < MathUtil.EPS_MEDIUM)
    assert(Math.abs(SAWTOOTH_WAVE.calculateOffset(Math.PI) - -1.0) < MathUtil.EPS_MEDIUM)
    assert(Math.abs(SAWTOOTH_WAVE.calculateOffset(2 * Math.PI) - 0.0) < MathUtil.EPS_MEDIUM)
  }

  test("triangle wave is absolute value of sawtooth") {
    val thetas = Seq(0.0, 0.3, 1.0, Math.PI / 2, Math.PI, 4.2, 2 * Math.PI)
    thetas.foreach { theta =>
      val expected = Math.abs(SAWTOOTH_WAVE.calculateOffset(theta))
      assert(Math.abs(TRIANGLE_WAVE3.calculateOffset(theta) - expected) < MathUtil.EPS_MEDIUM,
        s"theta=$theta")
    }
  }
}
