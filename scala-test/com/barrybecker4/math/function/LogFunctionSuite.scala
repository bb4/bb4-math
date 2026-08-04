/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.function

import scala.compiletime.uninitialized

/**
  * @author Barry Becker
  */
class LogFunctionSuite extends BaseFunctionSuite {
  /** instance under test */
  private var func: LogFunction = uninitialized

  test("TypicalFunc") {
    func = new LogFunction(10.0)
    assert(0.0 === func.getValue(1))
    assert(10.0 === func.getValue(10))
    // Math.log may differ in the last bit across JVMs
    assert(19.030899869919434 === func.getValue(80))
    assert(20.0 === func.getValue(100))
    assert(29.999999999999996 === func.getValue(1000))
  }

  test("Base2FuncWithSmallValues") {
    func = new LogFunction(0.1, 2.0, false)
    assert(0.0 === func.getValue(1))
    assert(0.3321928094887363 === func.getValue(10))
    assert(0.3 === func.getValue(8))
    assert(0.39999999999999997 === func.getValue(16))
    assert(-0.3 === func.getValue(0.125))
    assert(0.6643856189774726 === func.getValue(100))
    assert(0.9965784284662088 === func.getValue(1000))
  }

  test("Base2FuncNeverLessThan0") {
    func = new LogFunction(0.1, 2.0, true)
    assert(0.0 === func.getValue(1))
    assert(0.39999999999999997 === func.getValue(16))
    assert(0.0 === func.getValue(0.125))
    assert(0.6643856189774726 === func.getValue(100))
  }

  test("Base3Func positive only") {
    func = new LogFunction(1.0, 3.0, true)
    assert(0.0 === func.getValue(0.333))
    assert(func.getValue(-0.333).isNaN)
    assert(func.getValue(-27).isNaN)
    assert(func.getValue(0).isNaN)
  }

  test("FuncWithNegativeValuesPassedIn") {
    func = new LogFunction(10.0)
    assert(0.0 === func.getValue(1))
    assert(-10.0 === func.getValue(-10))
    assert(-19.030899869919434 === func.getValue(-80))
    assert(-20.0 === func.getValue(-100))
    assert(29.999999999999996 === func.getValue(1000))
  }

  test("FuncWithZeroScale") {
    func = new LogFunction(0.0)
    assert(0.0 === func.getValue(10))
  }
}
