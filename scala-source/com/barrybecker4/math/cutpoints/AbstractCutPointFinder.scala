/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.cutpoints

import com.barrybecker4.math.Range

import scala.collection.mutable.ArrayBuffer


/**
  * Base class for cut point finders.
  */
object AbstractCutPointFinder {
  /** The range should not get smaller than this. */
  private[cutpoints] val MIN_RANGE: Double = 1.0E-10
}

abstract class AbstractCutPointFinder {
  /**
    * Retrieve the cut point values.
    * If its a really small range include both min and max to avoid having just one label.
    * @param range range to be divided into intervals.
    * @param maxNumTicks upper limit on number of cut points to return.
    * @return the cut points
    */
  def getCutPoints(range: Range, maxNumTicks: Int): Array[Double] = {
    validateArguments(range, maxNumTicks)
    if (range.getExtent <= AbstractCutPointFinder.MIN_RANGE) tinyRangeCutPoints(range)
    else determineCutPoints(maxNumTicks, range)
  }

  /** For a zero or tiny span, return endpoints only (expanding a zero span slightly). */
  private def tinyRangeCutPoints(range: Range): Array[Double] = {
    val max =
      if (range.getExtent <= 0) range.min + AbstractCutPointFinder.MIN_RANGE
      else range.max
    Array(checkSmallNumber(range.min), checkSmallNumber(max))
  }

  private def determineCutPoints(maxTicks: Int, finalRange: Range): Array[Double] = {
    val extent = Rounder.roundUp(finalRange.getExtent)
    val step = Rounder.roundDown(extent / (maxTicks - 1))
    val roundedRange = Range(Math.floor(finalRange.min / step) * step, Math.ceil(finalRange.max / step) * step)
    val positions = new ArrayBuffer[Double]()
    addPoints(positions, roundedRange, finalRange, step)
    positions.toArray
  }

  protected def addPoints(positions: ArrayBuffer[Double], roundedRange: Range, finalRange: Range, d: Double): Unit

  /** Verify that the range and tick request are valid. */
  private def validateArguments(range: Range, maxNumTicks: Int): Unit = {
    require(maxNumTicks >= 2, s"maxNumTicks must be at least 2, but was $maxNumTicks")
    if (range.min.isNaN || range.min.isInfinite || range.max.isNaN || range.max.isInfinite)
      throw new IllegalArgumentException("Min or max of the range [" + range + "] is not a number.")
    if (range.getExtent.isNaN)
      throw new IllegalArgumentException("Min cannot be greater than max for " + range)
  }

  /** If real small just assume it is zero.
    * @param value the value to check.
    * @return zero if value is below the smallest allowed range, else return value.
    */
  private[cutpoints] def checkSmallNumber(value: Double): Double =
    if (Math.abs(value) < AbstractCutPointFinder.MIN_RANGE) 0 else value
}
