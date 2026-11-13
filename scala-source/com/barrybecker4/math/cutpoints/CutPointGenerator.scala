/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.cutpoints

import java.text.DecimalFormat

import com.barrybecker4.math.{MathUtil, Range}
import com.barrybecker4.math.cutpoints.AbstractCutPointFinder.MIN_RANGE

/**
  * Calculates nicely rounded intervals for a specified range.
  * From an article by Paul Heckbert in Graphics Gems 1.
  * @param useTightLabeling whether to use tight labeling.
  * @param formatter method for formatting the label values.
  */
class CutPointGenerator(initialTightLabeling: Boolean, val formatter: DecimalFormat) {

  private var tightLabeling: Boolean = initialTightLabeling
  private var cutPointFinder: AbstractCutPointFinder = finderFor(tightLabeling)

  def this() = {
    this(true, new DecimalFormat("###,###.##"))
  }

  /** Whether extreme labels use exact min/max (tight) vs rounded loose bounds. */
  def useTightLabeling: Boolean = tightLabeling

  def setUseTightLabeling(useTight: Boolean): Unit = {
    tightLabeling = useTight
    cutPointFinder = finderFor(useTight)
  }

  private def finderFor(useTight: Boolean): AbstractCutPointFinder =
    if (useTight) new TightCutPointFinder else new LooseCutPointFinder

  /** Retrieve the cut point values.
    * If its a really small range include both min and max to avoid having just one label.
    * @param range       range to be divided into intervals.
    * @param maxNumTicks upper limit on number of cut points to return.
    * @return the cut points
    */
  def getCutPoints(range: Range, maxNumTicks: Int): Array[Double] =
    cutPointFinder.getCutPoints(range, maxNumTicks)

  /** Labels for the found cut points.
    * @param range    tickmark range.
    * @param maxTicks upper limit on the number of cuts.
    * @return cut point labels
    */
  def getCutPointLabels(range: Range, maxTicks: Int): Array[String] = {
    val cutPoints = cutPointFinder.getCutPoints(range, maxTicks)
    val baseFracDigits = getNumberOfFractionDigits(range, maxTicks)
    cutPoints.indices.map(i => formatLabel(cutPoints(i), labelFractionDigits(baseFracDigits, i, cutPoints.length))).toArray
  }

  private def labelFractionDigits(baseFracDigits: Int, index: Int, length: Int): Int = {
    val extraForTightEnd = if (tightLabeling && (index == 0 || index == length - 1)) 1 else 0
    baseFracDigits + extraForTightEnd
  }

  private def formatLabel(value: Double, fracDigits: Int): String = {
    formatter.setMaximumFractionDigits(fracDigits)
    formatter.format(value)
  }

  /** Determine the number of fractional digits to show in the nice numbered cut points.
    * @param range       the range to check.
    * @param maxNumTicks no more than this many cut points.
    * @return Recommended number of fractional digits to display. The cut points: eg. 0, 1, 2, etc.
    */
  private[cutpoints] def getNumberOfFractionDigits(range: Range, maxNumTicks: Int): Int = {
    require(maxNumTicks >= 2, s"maxNumTicks must be at least 2, but was $maxNumTicks")
    val max1 = if (range.getExtent <= MIN_RANGE) range.min + MIN_RANGE else range.max
    val extent = Rounder.roundUp(max1 - range.min)
    val step = Rounder.roundDown(extent / (maxNumTicks - 1))
    Math.max(-Math.floor(MathUtil.log10(step)), 0).toInt
  }
}
