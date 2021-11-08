/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.function

import com.barrybecker4.math.Range


/**
  * @author Barry Becker
  */
class HeightFunctionSuite extends BaseFunctionSuite {

  /** instance under test */
  private var func: HeightFunction = _

  test("TypicalFunc(") {

    func = new HeightFunction(Array[Double](.2, 0.3, 0.6, 0.7, 0.5, 0.3, 0.11, 0.04, -0.1, -0.15), Range(1, 20))
    val exp = Array(0.2, 0.4473684, 0.065789, -0.15)
    val act = Array(1, 10, 15, 20).map(x => func.getValue(x.toDouble))
    verify(exp, act)
  }

  test("Base0Func") {
    func = new HeightFunction(Array[Double](.2, 0.31, 0.6, 0.7, 0.5, 0.3, 0.11, 0.04, -0.1, -0.15), Range(0, 9))
    val exp = Array(0.2, 0.31, 0.5, 0.04, -0.15)
    val act = Array(0, 1, 4, 7, 9).map(x => func.getValue(x.toDouble))
    verify(exp, act)
  }

  test("FuncFromRangeToBinsPositiveOffset") {
    func = new HeightFunction(Array[Double](.2, 0.3, 0.6, 0.7, 0.5, 0.2), Range(100, 500))
    val exp = Array(0.2, 0.225, 0.3, 0.20375, 0.4875, 0.2)
    val act = Array(100, 120, 180, 499, 230, 500).map(x => func.getValue(x.toDouble))
    verify(exp, act)
  }

  test("FuncFromRangeToBinsOutOfRange") {
    func = new HeightFunction(Array[Double](.2, 0.3, 0.6, 0.7, 0.5, 0.2), Range(100, 500))
    assertThrows[IllegalArgumentException] {
      func.getValue(501) // 0.2
    }
  }

  test("FuncFromRangeToBins") {
    func = new HeightFunction(Array[Double](.2, 0.3, 0.6, 0.7, 0.5, 0.2, 0.1, 0.5, 0.4, 0.3), Range(0, 9))
    assert(0.2 === func.getValue(0))
    assert(0.21 === func.getValue(0.1))
    assert(0.3 === func.getValue(1))
    assert(0.5 === func.getValue(4))
    assert(0.2 === func.getValue(5))
    assert(0.4 === func.getValue(8))
    assert(0.3 === func.getValue(9))
  }

  test("FuncFromAutoRangeToBins") {
    func = new HeightFunction(Array[Double](.2, 0.3, 0.6, 0.7, 0.5, 0.2, 0.1, 0.5, 0.4, 0.3))
    assert(0.2 === func.getValue(0))
    assert(0.29 === func.getValue(0.1))
    assert(0.405 === func.getValue(0.15))
    assert(0.432 === func.getValue(0.16))
    assert(0.16 === func.getValue(0.6))
    assert(0.39 === func.getValue(0.9))
    assert(0.327 === func.getValue(0.97))
    assert(0.3 === func.getValue(1.0))
  }
}
