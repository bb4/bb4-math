/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.interpolation

/**
  * Supported interpolators.
  * @author Barry Becker
  */
enum InterpolationMethod:

  case STEP, LINEAR, CUBIC, COSINE, HERMITE

  /**
    * Factory method for creating the interpolator to use.
    * Do not call the interpolator constructors directly when selecting by method.
    * @return the interpolator for this method
    */
  def createInterpolator(function: Array[Double]): Interpolator = this match
    case STEP => new StepInterpolator(function)
    case LINEAR => new LinearInterpolator(function)
    case CUBIC => new CubicInterpolator(function)
    case COSINE => new CosineInterpolator(function)
    case HERMITE => new HermiteInterpolator(function)

object InterpolationMethod:
  /** All methods in stable display order (used by UI combo boxes). */
  val VALUES: Array[InterpolationMethod] = InterpolationMethod.values

/** Package-level aliases so existing `import ...interpolation.LINEAR` keeps working. */
export InterpolationMethod.{STEP, LINEAR, CUBIC, COSINE, HERMITE}
