/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.function


/**
  * @author Barry Becker
  */
class CountFunctionSuite extends BaseFunctionSuite {

  /** instance under test */
  private var func: CountFunction = _


  test("BehaviorAfterConstruction")  {

    func = new CountFunction(0.3)

    val exp = Array(0.3, 0.3, 0.3)
    val act: Array[Double] = Array[Int](0, 10, 80).map(_.toDouble).map(func.getValue)
    verify(exp, act)
  }

  /** note that x values are normalized to 0 - 1 range.  */
  test("AddValue(") {
    func = new CountFunction(2.1)
    assert(func.getValue(0) === 2.1)
    func.addValue(1, 5.3)
    assert(func.getValue(1) === 5.3)
    assert(func.getValue(0.5) === 3.7)
    func.addValue(2, 7.3)
    func.addValue(3, 4.3)
    assert(func.getValue(1) === 4.3)
  }

  test("AddValueWheMaxSet") {
    func = new CountFunction(3.0)
    func.setMaxXValues(5)
    func.addValue(1, 5)
    func.addValue(3, 8)
    func.addValue(4, 10)
    assert(func.getValue(0) === 3.0)
    assert(func.getValue(0.2) === 4.2)
    assert(func.getValue(0.5) === 6.5)
    assert(func.getValue(1.0) === 10.0)
    func.addValue(5, 11)
    assert(func.getValue(0.2) === 4.6)
    func.addValue(6, 12)
    func.addValue(7, 14)
    // not that the early x values have been removed.
    assert(func.getValue(0.2) === 8.0)
    assert(func.getValue(0.5) === 10.5)
    assert(func.getValue(1.0) === 14.0)
  }

  test("SetInitialValue") {
    func = new CountFunction(3.0)
    func.addValue(1, 5)
    func.addValue(3, 8)
    func.addValue(4, 10)
    assert(func.getValue(0) === 3.0)
    assert(func.getValue(0.2) === 4.2)
    assert(func.getValue(0.5) === 6.5)
    assert(func.getValue(1.0) == 10.0)
    func.setInitialValue(11)
    assert(func.getValue(0.2) === 11.0)
    func.addValue(6, 12)
    func.addValue(7, 14)
    assert(func.getValue(0.2) === 11.4)
    assert(func.getValue(0.5) === 12.0)
    assert(func.getValue(1.0) === 14.0)
  }
}
