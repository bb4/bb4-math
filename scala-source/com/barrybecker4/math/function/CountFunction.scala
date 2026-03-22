/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.function

import com.barrybecker4.math.Range

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object CountFunction {
  /** When we get more than this many x values, scroll to the right instead of compressing the domain. */
  private val DEFAULT_MAX_X_VALUES: Int = 500
}

/**
  * Tracks a positive integer over time. For example, it might be used to track a population count.
  * @param initialYValue y value for x=0
  * @author Barry Becker
  */
class CountFunction(val initialYValue: Double) extends Function {
  protected val xValues: ListBuffer[Double] = ListBuffer.empty
  protected val yValues: ListBuffer[Double] = ListBuffer.empty
  private var maxXValues: Int = CountFunction.DEFAULT_MAX_X_VALUES
  setInitialValue(initialYValue)

  def setInitialValue(initialYValue: Double): Unit = {
    xValues.clear()
    yValues.clear()
    xValues.append(0.0)
    yValues.append(initialYValue)
  }

  def addValue(x: Double, y: Double): Unit = {
    if (xValues.size > maxXValues) {
      xValues.remove(0)
      yValues.remove(0)
    }
    xValues.append(x)
    yValues.append(y)
    renormalizeXToUnitInterval()
  }

  def setMaxXValues(max: Int): Unit = {
    maxXValues = max
  }

  override def getDomain: Range = Range(xValues.head, xValues.last)

  override def getValue(xValue: Double): Double =
    interpolateY(xValue, xValues, yValues)

  private def renormalizeXToUnitInterval(): Unit = {
    val len = xValues.size
    var i = 0
    while (i < len) {
      xValues(i) = i.toDouble / (len - 1).max(1)
      i += 1
    }
  }

  private def interpolateY(xValue: Double, xVals: mutable.Seq[Double], yVals: mutable.Seq[Double]): Double = {
    var i = 0
    while (i < xVals.size && xValue > xVals(i)) i += 1
    if (i == 0 || xVals.size == 1) yVals.head
    else {
      val xValm1 = xVals(i - 1)
      val denom = xVals(i) - xValm1
      if (denom == 0) yVals(i - 1)
      else {
        val ratio = (xValue - xValm1) / denom
        val yValm1 = yVals(i - 1)
        yValm1 + ratio * (yVals(i) - yValm1)
      }
    }
  }

  override def toString: String = {
    val bldr = new StringBuilder("CountFunction: ")
    for (i <- xValues.indices)
      bldr.append("\nx=").append(xValues(i)).append(" y=").append(yValues(i))
    bldr.toString
  }
}
