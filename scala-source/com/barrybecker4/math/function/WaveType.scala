/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.function

/**
  * Different sorts of wave forms.
  * @author Barry Becker
  */
enum WaveType(name: String):

  case SINE_WAVE extends WaveType("Sine Wave")
  case SQUARE_WAVE extends WaveType("Square Wave")
  case SAWTOOTH_WAVE extends WaveType("Sawtooth Wave")
  case TRIANGLE_WAVE3 extends WaveType("Triangle Wave")

  def calculateOffset(theta: Double): Double = this match
    case SINE_WAVE => Math.sin(theta)
    case SQUARE_WAVE => if Math.sin(theta) > 0.0 then 1.0 else -1.0
    case SAWTOOTH_WAVE =>
      val t = theta / Math.PI / 2
      2 * (t - Math.floor(t + 0.5))
    case TRIANGLE_WAVE3 => Math.abs(SAWTOOTH_WAVE.calculateOffset(theta))

  override def toString: String = name

object WaveType:
  /** All wave types in stable display order (used by UI combo boxes). */
  val VALUES: Array[WaveType] = WaveType.values

/** Package-level aliases so existing `import ...function.SINE_WAVE` keeps working. */
export WaveType.{SINE_WAVE, SQUARE_WAVE, SAWTOOTH_WAVE, TRIANGLE_WAVE3}
