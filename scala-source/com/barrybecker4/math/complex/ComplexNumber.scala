/* Copyright by Barry G. Becker, 2000-2019. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.complex

/**
  * A Complex number is comprised of real and imaginary parts of the form a + bi.
 *
  */
object ComplexNumber {
  val i: ComplexNumber = ComplexNumber(0, 1.0)
  val NaN: ComplexNumber = ComplexNumber(Double.NaN, Double.NaN)

  /**
    * Divide c1 by c2.
    * The textbook formula is used when every product fits in a Double, which keeps
    * moderate inputs bit-for-bit with the historical result. When `c² + d²` (or a
    * numerator product) overflows, Smith's method scales by the larger divisor
    * component so `|c2|²` is never formed.
    * @return the result of dividing c1 by c2.
    */
  def divide(c1: ComplexNumber, c2: ComplexNumber): ComplexNumber = {
    val a = c1.real
    val b = c1.imaginary
    val c = c2.real
    val d = c2.imaginary
    val denom = c * c + d * d
    val realNum = a * c + b * d
    val imagNum = b * c - a * d
    if (realNum.isFinite && imagNum.isFinite && denom.isFinite)
      ComplexNumber(realNum / denom, imagNum / denom)
    else
      divideScaled(a, b, c, d)
  }

  /** Smith's complex division. `c` and `d` are the divisor's real and imaginary parts. */
  private def divideScaled(a: Double, b: Double, c: Double, d: Double): ComplexNumber = {
    if (Math.abs(d) <= Math.abs(c)) {
      val ratio = d / c
      val den = c + d * ratio
      ComplexNumber((a + b * ratio) / den, (b - a * ratio) / den)
    } else {
      val ratio = c / d
      val den = d + c * ratio
      ComplexNumber((a * ratio + b) / den, (b * ratio - a) / den)
    }
  }

  /** @return the sum of two complex numbers.*/
  def add(c1: ComplexNumber, c2: ComplexNumber) = ComplexNumber(c1.real + c2.real, c1.imaginary + c2.imaginary)

  /** @return result of subtracting two ComplexNumber from this one.*/
  def subtract(c1: ComplexNumber, c2: ComplexNumber) = ComplexNumber(c1.real - c2.real, c1.imaginary - c2.imaginary)

  /** @return result of Multiplying two complex numbers.*/
  def multiply(c1: ComplexNumber, c2: ComplexNumber) =
    ComplexNumber(c1.real * c2.real - c1.imaginary * c2.imaginary, c1.real * c2.imaginary + c1.imaginary * c2.real)

  /** See https://www.math.toronto.edu/mathnet/questionCorner/complexexp.html
    * @return the result of raising a to some complex number c. Here, a is a real number.
    */
  def pow(a: Double, c: ComplexNumber): ComplexNumber = {
    val a1 = Math.pow(a, c.real)
    val clna = c.imaginary * Math.log(a)
    ComplexNumber(a1 * Math.cos(clna), a1 * Math.sin(clna))
  }

  /**
    * See http://mathworld.wolfram.com/ComplexExponentiation.html
    * @return the result of raising z to some complex number s. Here, z is a complex number.
    */
  def pow(z: ComplexNumber, s: ComplexNumber): ComplexNumber = {
    if (z.imaginary == 0) pow(z.real, s)
    else {
      val a = z.real
      val b = z.imaginary
      val c = s.real
      val d = s.imaginary
      val asqpbsq = a * a + b * b
      val compArg = arg(z)
      val coef = Math.pow(asqpbsq, c / 2.0) * Math.exp(-d * compArg)
      val term = c * compArg + 0.5 * d * Math.log(asqpbsq)
      val realPart = coef * Math.cos(term)
      val imgPart = coef * Math.sin(term)
      ComplexNumber(realPart, imgPart)
    }
  }

  /** See http://mathworld.wolfram.com/ComplexArgument.html
    * @return the complex argument of z in (-π, π]
    */
  def arg(z: ComplexNumber): Double =
    Math.atan2(z.imaginary, z.real)

  /** See https://proofwiki.org/wiki/Sine_of_Complex_Number
    * sin(a+bi) = sina coshb + i cosa sinhb
    */
  def sin(c: ComplexNumber): ComplexNumber = {
    val realPart = Math.sin(c.real) * Math.cosh(c.imaginary)
    val imgPart = Math.cos(c.real) * Math.sinh(c.imaginary)
    ComplexNumber(realPart, imgPart)
  }

  /** See https://proofwiki.org/wiki/Cosine_of_Complex_Number
    * cos(a+bi) = cosa coshb − i sina sinhb
    */
  def cos(c: ComplexNumber): ComplexNumber = {
    val realPart = Math.cos(c.real) * Math.cosh(c.imaginary)
    val imgPart = -Math.sin(c.real) * Math.sinh(c.imaginary)
    ComplexNumber(realPart, imgPart)
  }
}

case class ComplexNumber(real: Double, imaginary: Double = 0) {

  /**
    * Euclidean modulus `|z| = sqrt(real² + imaginary²)`.
    * Uses `hypot` so large components do not overflow.
    */
  def abs: Double = Math.hypot(real, imaginary)

  /**
    * Squared Euclidean modulus `|z|² = real² + imaginary²`.
    * Prefer this over [[abs]] when only the square is needed (for example in [[reciprocal]]),
    * since it avoids a square-root.
    */
  def absSquared: Double = real * real + imaginary * imaginary

  /**
    * @deprecated use [[abs]]; kept for binary/source compatibility with published callers.
    */
  @deprecated("use abs", since = "2.1")
  def getMagnitude: Double = abs

  /**
    * @deprecated use [[absSquared]]; this formerly returned `|z|²`, which is easy to confuse with [[abs]].
    */
  @deprecated("use absSquared", since = "2.1")
  def magnitude: Double = absSquared

  def negate: ComplexNumber = ComplexNumber(-real, -imaginary)

  /** @return the sum of this number plus another. */
  def add(other: ComplexNumber): ComplexNumber = ComplexNumber.add(this, other)
  def add(other: Double): ComplexNumber = ComplexNumber(real + other, imaginary)

  /** @return result of subtracting another ComplexNumber from this one. */
  def subtract(other: ComplexNumber): ComplexNumber = ComplexNumber.subtract(this, other)
  def subtract(other: Double): ComplexNumber = ComplexNumber(real - other, imaginary)

  /** @return result of multiplying this Complex times another one.  */
  def multiply(other: ComplexNumber): ComplexNumber = ComplexNumber.multiply(this, other)
  def multiply(other: Double): ComplexNumber = ComplexNumber(real * other, imaginary * other)

  /** @return result of dividing this Complex times another one.  */
  def divide(other: ComplexNumber): ComplexNumber = ComplexNumber.multiply(this, other.reciprocal)
  def divide(other: Double): ComplexNumber = ComplexNumber(real / other, imaginary / other)

  /** @return this complex number raised to the power n, where n is a non-negative integer */
  def pow(n: Int): ComplexNumber =
    if (n < 0)
      throw IllegalArgumentException(s"Negative integer power not supported: $n")
    else if (n == 0) ComplexNumber(1)
    else (1 until n).foldLeft(this)((v, _) => v.multiply(this))

  /**
    * @return the reciprocal, 1/c.
    * When `real² + imaginary²` overflows, fall back to Smith's scaled division
    * so a large finite input does not collapse to zero.
    */
  def reciprocal: ComplexNumber = {
    val mag2 = absSquared
    if (mag2.isFinite) ComplexNumber(real / mag2, -imaginary / mag2)
    else ComplexNumber.divide(ComplexNumber(1), this)
  }

  def isNaN: Boolean = real.isNaN || imaginary.isNaN
  def isInfinite: Boolean = real.isInfinite || imaginary.isInfinite

  /** @param exponent integer power to raise to (same semantics as [[pow]]) */
  def power(exponent: Int): ComplexNumber = pow(exponent)

  override def toString: String = {
    if (imaginary == 0) s"$real"
    else if (real == 0) s"${imaginary}i"
    else if (imaginary < 0) s"$real - ${-imaginary}i"
    else s"$real + ${imaginary}i"
  }
}
