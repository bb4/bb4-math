/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.cutpoints

import com.barrybecker4.math.Range
import com.barrybecker4.math.cutpoints.TightCutPointFinder._

import scala.collection.mutable.ArrayBuffer

/**
  * The min and max cut-points will be specific full precision numbers.
  */
object TightCutPointFinder {
  /** Labels should not get closer to each other than this percentage of total. */
  private val MIN_LABEL_SEPARATION_PCT = 0.2
}

class TightCutPointFinder extends AbstractCutPointFinder {

  override protected def addPoints(positions: ArrayBuffer[Double],
                                   roundedRange: Range, finalRange: Range, d: Double): Unit = {
    positions.append(checkSmallNumber(finalRange.min))
    for (x <- interiorTicks(roundedRange, finalRange, d))
      positions.append(checkSmallNumber(x))
    positions.append(checkSmallNumber(finalRange.max))
  }

  /** Nice-number ticks between the exact endpoints, skipping any that crowd an end. */
  private def interiorTicks(roundedRange: Range, finalRange: Range, d: Double): Seq[Double] = {
    val start = roundedRange.min + initialIncrement(roundedRange, finalRange, d)
    val stop = roundedRange.max - finalIncrement(roundedRange, finalRange, d)
    scala.collection.immutable.Range.BigDecimal(start, stop, d).map(_.toDouble)
  }

  private def initialIncrement(roundedRange: Range, finalRange: Range, d: Double): Double = {
    val pctStart = (roundedRange.min + d - finalRange.min) / d
    if (pctStart < MIN_LABEL_SEPARATION_PCT) 2 * d else d
  }

  private def finalIncrement(roundedRange: Range, finalRange: Range, d: Double): Double = {
    val pctEnd = (finalRange.max - (roundedRange.max - d)) / d
    if (pctEnd < MIN_LABEL_SEPARATION_PCT) 1.5 * d else 0.5 * d
  }
}
