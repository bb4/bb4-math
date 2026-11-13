/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.cutpoints

import com.barrybecker4.math.Range
import com.barrybecker4.math.cutpoints.NiceNumbersSuite._
import org.scalatest.funsuite.AnyFunSuite

import java.text.DecimalFormat


object NiceNumbersSuite {
  private val EXPECTED_LOOSE_CUTS = Array("0", "20", "40", "60", "80", "100", "120")
  private val EXPECTED_TIGHT_CUTS = Array("11", "20", "40", "60", "80", "101")
  private val EXPECTED_LOOSE_CUTS2 = Array("11.05", "11.1", "11.15", "11.2", "11.25")
  private val EXPECTED_TIGHT_CUTS2 = Array("11.1", "11.15", "11.2", "11.23")
  private val EXPECTED_LOOSE_CUTS3 = Array("-2", "0", "2", "4", "6")
  private val EXPECTED_TIGHT_CUTS3_PREFIX = Array("-1.5", "0", "2", "4")
  private val EXPECTED_LOOSE_CUTS4 = Array("-1.5", "-1", "-0.5", "0", "0.5", "1", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5")
  private val EXPECTED_TIGHT_CUTS4 = Array("-1.48", "-1", "-0.5", "0", "0.5", "1", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "4.61")
}

class NiceNumbersSuite extends AnyFunSuite {

  private def generator(tight: Boolean = true) =
    new CutPointGenerator(tight, new DecimalFormat("###,###.##"))

  test("NiceNumbers1") {
    val range = Range(11.0, 101.0)
    assert(EXPECTED_LOOSE_CUTS === generator(false).getCutPointLabels(range, 5))
    assert(EXPECTED_TIGHT_CUTS === generator(true).getCutPointLabels(range, 5))
  }

  test("NiceNumbers2") {
    val range = Range(11.1, 11.23)
    assert(EXPECTED_LOOSE_CUTS2 === generator(false).getCutPointLabels(range, 5))
    assert(EXPECTED_TIGHT_CUTS2 === generator(true).getCutPointLabels(range, 5))
  }

  test("NiceNumbers3") {
    val range = Range(-1.475879149417865, 4.609775340085392)
    assert(EXPECTED_LOOSE_CUTS3 === generator(false).getCutPointLabels(range, 5))
    val resultTight = generator(true).getCutPointLabels(range, 5)
    assert(EXPECTED_TIGHT_CUTS3_PREFIX === resultTight.init)
    // Last tick: max fraction digits can be 1 or 2 depending on FP in getNumberOfFractionDigits / nice steps
    assert(resultTight.last == "4.6" || resultTight.last == "4.61")
  }

  test("NiceNumbers4") {
    val range = Range(-1.475879149417865, 4.609775340085392)
    assert(EXPECTED_LOOSE_CUTS4 === generator(false).getCutPointLabels(range, 20))
    assert(EXPECTED_TIGHT_CUTS4 === generator(true).getCutPointLabels(range, 20))
  }

  /** There should be no fractional digits in this case */
  test("FracDigitsWhenIntegerEndPoints") {
    val expectedNumFractDigits = 0
    val g = generator()
    assert(expectedNumFractDigits === g.getNumberOfFractionDigits(Range(10, 100), 3))
    assert(expectedNumFractDigits === g.getNumberOfFractionDigits(Range(1000, 100000), 10))
    assert(expectedNumFractDigits === g.getNumberOfFractionDigits(Range(-1000, -100), 300))
  }

  test("FracDigitsWithHighMaxTicks") {
    val f = generator().getNumberOfFractionDigits(Range(-0.002, 0.001), 900)
    assert(6.0 === f)
  }

  test("FracDigitsInThirdDecimalPlace200Ticks") {
    val f = generator().getNumberOfFractionDigits(Range(-0.002, 0.001), 200)
    assert(5.0 === f)
  }

  test("FracDigitsInThirdDecimalPlace2Ticks") {
    val f = generator().getNumberOfFractionDigits(Range(-0.002, 0.001), 2)
    assert(3.0 === f)
  }

  test("FracDigitsSixthDecimalPlace30Ticks") {
    val f = generator().getNumberOfFractionDigits(Range(0.0000001, 0.0001), 30)
    assert(6.0 === f)
  }

  test("FracDigitsSixthDecimalPlace5Ticks") {
    val f = generator().getNumberOfFractionDigits(Range(0.0000001, 0.0001), 5)
    assert(5.0 === f)
  }

  test("FracDigits4") {
    val f = generator().getNumberOfFractionDigits(Range(-100.01, -100.001), 30)
    assert(4.0 === f)
  }
}
