/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.function

import com.barrybecker4.math.{MathUtil, Range}
import com.barrybecker4.math.interpolation.LinearInterpolator

/**
  * Gaussian error function
  * @author Barry Becker
  */
object ErrorFunction {

  private val MAX_ERROR_FUNCTION_TABLE_VALUE = 5.3

  /** The gaussian error function table.
    * See http://eceweb.uccs.edu/Wickert/ece3610/lecture_notes/erf_tables.pdf
    * for values of x  = 0.0, 0.1, ... MAX_ERROR_FUNCTION_TABLE_VALUE
    * Try plotting this in log scale to help understanding.
    */
  private val ERROR_FUNCTION = Array(
    0.0000000, 0.0563721, 0.11246296, 0.16800, 0.2227026, 0.27633, 0.3286268, 0.37938, 0.4283924, 0.47548, 0.5204999,
    0.56332, 0.6038561, 0.64203, 0.6778012, 0.71116, 0.7421008, 0.77067, 0.7969081, 0.82089, 0.8427007, 0.86244,
    0.883533, 0.89612, 0.910314, 0.92290, 0.9340079, 0.94376, 0.9522851, 0.95970, 0.9661051, 0.97162, 0.9763484,
    0.98038, 0.9837905, 0.98667, 0.9890905, 0.99111, 0.9927904, 0.99418, 0.99532213, 0.996258, 0.9970205, 0.99764,
    0.9981372, 0.99854, 0.9988568, 0.99911, 0.9993115, 0.99947, 0.9995901, 0.99969, 0.99976, 0.99982, 0.99987,
    0.9999, 0.99992, 0.99994, 0.99995887, 0.99996977878, 0.999977894, 0.999983, 0.999988, 0.999991, 0.9999931,
    0.9999957, 0.9999975, 0.9999980, 0.9999984, 0.99999870, 0.99999930, 0.99999970, 0.999999840, 0.999999901,
    0.9999999350, 0.999999860, 0.999999910, 0.999999931, 0.999999955, 0.999999971,
    //    4.0      4.05       4.1         4.15      4.2         4.25       4.3         4.35       4.4         4.45          4.5         4.55        4.6            4.65         4.7              4.75          4.8             4.85            4.9           4.95
    0.99999998453, 0.999999988, 0.999999993279, 0.999999995601, 0.99999999713, 0.99999999814275, 0.999999998802, 0.999999999231, 0.9999999995088, 0.99999999968776, 0.9999999998024914, 0.999999999875673, 0.9999999999221209, 0.999999999951454, 0.9999999999698866,
    0.9999999999814, 0.9999999999885819, 0.9999999999930206, 0.9999999999957546, 0.9999999999974303, 0.99999999999845, 0.9999999999988, 0.99999999999945, 0.9999999999996, 0.999999999999806, 0.9999999999996, 0.99999999999994)
  /** for values of x  = 0.0, 0.1, ... 1.0 */
  private val INVERSE_ERROR_FUNCTION = Array(0.0, 0.089, 0.18, 0.28, 0.379, 0.479, 0.596, 0.738, 0.91, 1.161, 3.28)
}

class ErrorFunction() extends InvertibleFunction {

  private val interpolator = new LinearInterpolator(ErrorFunction.ERROR_FUNCTION)
  private val inverseInterpolator = new LinearInterpolator(ErrorFunction.INVERSE_ERROR_FUNCTION)

  /** We expect x to be in the range approximately 0.0, 5.0.
    * Values outside of -MAX_ERROR_FUNCTION_TABLE_VALUE to MAX_ERROR_FUNCTION_TABLE_VALUE are
    * Currently just using simple linear interpolation.
    * We could improve by using quadratic interpolation.
    * @param x x value to get y for
    * @return error function value for x.
    */
  override def getValue(x: Double): Double = {
    val sign = if (x >= 0) 1.0
    else -1.0
    if (Math.abs(x) > ErrorFunction.MAX_ERROR_FUNCTION_TABLE_VALUE) {
      val v = 1.0 - (100.0 - Math.abs(x)) * MathUtil.EPS_MEDIUM
      if (x > 50) System.out.println("erf(" + x + ")=" + v) //NON-NLS
      assert(v > 0.0, " x=" + x + " v=" + v)
      return sign * v
    }
    sign * interpolator.interpolate(Math.abs(x) / ErrorFunction.MAX_ERROR_FUNCTION_TABLE_VALUE)
  }

  /** We expect x to be in the range [-1.0, 1.0].
    * Currently just using simple linear interpolation.
    * We could improve by using quadratic interpolation.
    * @param x inverse error function value for x.
    * @return inverse error function value for x.
    */
  override def getInverseValue(x: Double): Double = {
    assert(x >= -1.0 && x <= 1.0)
    val sign = if (x >= 0) 1.0
    else -1.0
    sign * inverseInterpolator.interpolate(Math.abs(x))
  }

  override def getDomain = Range(-5.0, 5.0)
}
