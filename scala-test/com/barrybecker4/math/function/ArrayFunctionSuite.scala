/* Copyright by Barry G. Becker, 2020 Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.function

import com.barrybecker4.math.Range
import com.barrybecker4.math.interpolation.{COSINE, LINEAR}


class ArrayFunctionSuite extends BaseFunctionSuite {

  /** instance under test */
  private val func: ArrayFunction = new ArrayFunction(Array[Double](.1, 0.11, 0.5, 0.95, 1.0))

  test("getValues") {
    val exp = Array(0.104, 1.0, 0.1, 0.5, 0.98)
    val act = Array(0.1, 1.0, 0, 0.5, 0.9).map(func.getValue)
    verify(exp, act)
  }

  test("invertValues") {
    val exp = Array(0.1358974, 1.0, 0.1413333333, 0.5, 0.9711111111)
    val act = Array(0.1, 1.0, 0.104, 0.5, 0.98).map(func.getInverseValue)
    verify(exp, act)
  }

  test("getDomain") {
    assertResult(Range(0, 1.0)) { func.getDomain }
  }

  test("fromSamples accepts non-monotonic data") {
    val samples = Array(1.0, 0.0, 1.0)
    val f = ArrayFunction.fromSamples(samples, LINEAR)
    assert(f.functionMap eq samples)
    assert(f.inverseFunctionMap sameElements ArrayFunction.identityMap(3))
    assert(f.getValue(0.0) === 1.0)
    assert(f.getValue(0.5) === 0.0)
    assert(f.getValue(1.0) === 1.0)
  }

  test("withInterpolationMethod preserves maps and changes method") {
    val samples = Array(0.0, 0.5, 1.0)
    val base = ArrayFunction.fromSamples(samples, LINEAR)
    val cosine = base.withInterpolationMethod(COSINE)
    assert(cosine.functionMap eq base.functionMap)
    assert(cosine.inverseFunctionMap eq base.inverseFunctionMap)
    assert(cosine.interpMethod == COSINE)
    assert(base.interpMethod == LINEAR)
  }

  test("withInterpolationMethod changes midpoint for COSINE vs LINEAR") {
    // Quarter point between 0 and 1: LINEAR = 0.25; COSINE smoothstep differs.
    val samples = Array(0.0, 1.0)
    val linear = ArrayFunction.fromSamples(samples, LINEAR)
    val cosine = linear.withInterpolationMethod(COSINE)
    assert(linear.getValue(0.25) === 0.25)
    assert(math.abs(cosine.getValue(0.25) - linear.getValue(0.25)) > 1e-6)
  }

  test("monotonic constructor still computes inverse") {
    val samples = Array(0.0, 0.5, 1.0)
    val f = new ArrayFunction(samples, LINEAR)
    assert(f.getValue(0.5) === 0.5)
    assert(f.getInverseValue(0.5) === 0.5)
    assert(f.getInverseValue(0.0) === 0.0)
    assert(f.getInverseValue(1.0) === 1.0)
  }
}
