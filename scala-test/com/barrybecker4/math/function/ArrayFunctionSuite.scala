/* Copyright by Barry G. Becker, 2020 Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.function

import com.barrybecker4.math.Range


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

}
