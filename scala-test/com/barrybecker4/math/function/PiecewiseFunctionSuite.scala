/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.function

import org.scalatest.funsuite.AnyFunSuite

class PiecewiseFunctionSuite extends AnyFunSuite {

  private val xs = Array(0.0, 1.0, 2.0)
  private val ys = Array(0.0, 10.0, 30.0)

  test("getValue interpolates y from x") {
    val f = new PiecewiseFunction(xs, ys)
    assert(f.getValue(0.5) === 5.0)
    assert(f.getValue(1.5) === 20.0)
  }

  test("getInverseValue interpolates x from y (strictly increasing y)") {
    val f = new PiecewiseFunction(xs, ys)
    assert(f.getInverseValue(5.0) === 0.5)
    assert(f.getInverseValue(20.0) === 1.5)
  }

  test("extrapolation clamps to endpoints") {
    val f = new PiecewiseFunction(xs, ys)
    assert(f.getValue(-1.0) === 0.0)
    assert(f.getValue(5.0) === 30.0)
    assert(f.getInverseValue(-1.0) === 0.0)
    assert(f.getInverseValue(100.0) === 2.0)
  }
}
