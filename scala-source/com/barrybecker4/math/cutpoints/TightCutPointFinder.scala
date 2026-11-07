/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.cutpoints

import com.barrybecker4.math.Range
import com.barrybecker4.math.cutpoints.TightCutPointFinder._

import scala.collection.mutable.ArrayBuffer

/**
  * The min and max cut-points will be specific full precision numbers.
  *
  */
object TightCutPointFinder {
  /** Labels should not get closer to each other than this percentage of total. */
  private val MIN_LABEL_SEPARATION_PCT = 0.2
}

class TightCutPointFinder extends AbstractCutPointFinder {

  override protected def addPoints(positions: ArrayBuffer[Double],
                                   roundedRange: Range, finalRange: Range, d: Double): Unit = {
    positions.append(checkSmallNumber(finalRange.min))
    val pctStart = (roundedRange.min + d - finalRange.min) / d
    val initialInc = if (pctStart < MIN_LABEL_SEPARATION_PCT) 2 * d else d
    val pctEnd = (finalRange.max - (roundedRange.max - d)) / d
    val finalInc = if (pctEnd < MIN_LABEL_SEPARATION_PCT) 1.5 * d else 0.5 * d
    val stop = roundedRange.max - finalInc

    for (x <- scala.collection.immutable.Range.BigDecimal(roundedRange.min + initialInc, stop, d))
      positions.append(checkSmallNumber(x.toDouble))

    positions.append(checkSmallNumber(finalRange.max))
  }
}