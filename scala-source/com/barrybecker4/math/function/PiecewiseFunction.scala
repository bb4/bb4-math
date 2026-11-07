/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.function

import com.barrybecker4.math.Range

/**
  * Piecewise linear function representation.
  * These parallel arrays define the piecewise function map.
  * `xValues` must be strictly increasing. For [[getInverseValue]], `yValues` must also be strictly
  * increasing so that the mapping is invertible along the sampled segments.
  *
  * @param xValues x function values
  * @param yValues y function values
  */
class PiecewiseFunction(val xValues: Array[Double], val yValues: Array[Double]) extends InvertibleFunction {
  assert(this.xValues.length == this.yValues.length)
  assert(this.xValues.length >= 1)

  override def getValue(value: Double): Double =
    interpolateAlong(xValues, yValues, value)

  override def getInverseValue(value: Double): Double =
    interpolateAlong(yValues, xValues, value)

  override def getDomain: Range = Range(xValues.head, xValues.last)

  /** Linear interpolation: given abscissa samples and ordinate samples, find ordinate at `t`. */
  private def interpolateAlong(abscissa: Array[Double], ordinate: Array[Double], t: Double): Double = {
    var i = 0
    val n = abscissa.length
    while (i < n && t > abscissa(i)) i += 1
    if (i == 0) ordinate(0)
    else if (i >= n) ordinate(n - 1)
    else {
      val t0 = abscissa(i - 1)
      val denom = abscissa(i) - t0
      if (denom == 0) ordinate(i - 1)
      else {
        val ratio = (t - t0) / denom
        ordinate(i - 1) + ratio * (ordinate(i) - ordinate(i - 1))
      }
    }
  }

  override def toString: String = {
    val bldr = new StringBuilder("PiecewiseFunction: ")
    for (i <- xValues.indices)
      bldr.append("x=").append(xValues(i)).append(" y=").append(yValues(i))
    bldr.toString
  }
}
