/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.function

import org.scalactic.{Equality, TolerantNumerics}
import org.scalatest.{BeforeAndAfterEach, FunSuite}

/**
  * Base test class for function classes.
  * @author Barry Becker
  */
abstract class FunctionSuiteBase extends FunSuite with BeforeAndAfterEach {

  /** function class under test. */
  protected var function: InvertibleFunction = _
  implicit val doubleEq: Equality[Double] =
    TolerantNumerics.tolerantDoubleEquality(0.00000000000001)

  override def beforeEach(): Unit = {
    function = createFunction
  }

  protected def createFunction: InvertibleFunction

  test("GetFunctionValue") {
    var y = function.getValue(0.1)
    assert(getExpectedValue0_1 === y)
    y = function.getValue(0.9)
    assert(getExpectedValue0_9 === y)
  }

  protected def getExpectedValue0_1: Double
  protected def getExpectedValue0_9: Double

  test("GetInverseFunctionValue") {
    var y = function.getInverseValue(0.1)
    assert(getExpectedInverseValue0_1 === y)
    y = function.getValue(0.9)
    assert(getExpectedInverseValue0_9 === y)
  }

  protected def getExpectedInverseValue0_1: Double
  protected def getExpectedInverseValue0_9: Double
}
