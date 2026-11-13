/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.linear

import com.barrybecker4.math.MathUtil

/**
  * Represents an immutable N dimensional vector.
  */
case class Vector(data: IndexedSeq[Double]) {
  assert(data.nonEmpty)

  def set(i: Int, value: Double): Vector =
    Vector(data.updated(i, value))

  def apply(i: Int): Double = data(i)

  /** Find the dot product of ourselves with another vector.
    * @return the dot product with another vector
    */
  def dot(b: Vector): Double = {
    checkDims(b)
    data.zip(b.data).map { case (x, y) => x * y }.sum
  }

  /** Find the normalized dot product with range [-1, 1].
    * @param b vector to dot product with
    * @return the normalized dot product.
    */
  def normalizedDot(b: Vector): Double = {
    val magB = b.magnitude
    val magThis = this.magnitude

    val product = magThis * magB
    val divisor = if (product == 0) 1.0 else product
    val dot = this.dot(b)
    val normalizedDotProduct = dot / divisor
    assert(normalizedDotProduct >= (-1.0 - MathUtil.EPS_MEDIUM) && normalizedDotProduct <= (1 + MathUtil.EPS_MEDIUM),
      "Normalized Dot product, " + normalizedDotProduct + ", was outside expected range.\nDot=" + dot + " div=" +
        divisor + "\nfor v1=" + this + "v2=" + b + "\nmagThis=" + magThis + " magB=" + magB)
    normalizedDotProduct
  }

  /** @param factor amount to scale by.
    * @return this Vector, scaled by a constant factor
    */
  def scale(factor: Double): Vector = Vector(data.map(_ * factor))

  /** @return pairwise sum of this Vector a and b */
  def plus(b: Vector): Vector = {
    checkDims(b)
    Vector(data.zip(b.data).map { case (x, y) => x + y })
  }

  /** @param b vector to find distance to.
    * @return Euclidean distance between this Vector and b
    */
  def distanceTo(b: Vector): Double = {
    checkDims(b)
    Math.sqrt(data.zip(b.data).map {case (x, y) => (x - y) * (x - y)}.sum)
  }

  /**
    * @return magnitude of the vector.
    * Scales by the largest absolute component before summing squares so that
    * very large or very small components do not overflow or underflow to a
    * useless magnitude (which would make [[normalize]] collapse to zero).
    */
  def magnitude: Double = {
    val maxAbs = data.foldLeft(0.0)((m, x) => Math.max(m, Math.abs(x)))
    if (maxAbs == 0.0) 0.0
    else {
      val sumSq = data.map { x => val t = x / maxAbs; t * t }.sum
      maxAbs * Math.sqrt(sumSq)
    }
  }

  /** @return a vector in the same direction as this, but with unit magnitude (or zero if [[magnitude]] is 0). */
  def normalize: Vector = {
    val mag = this.magnitude
    if (mag == 0) Vector(data.map(_ => 0.0))
    else Vector(data.map(_ / mag))
  }

  def size: Int = data.length

  private def checkDims(b: Vector): Unit =
    if (this.size != b.size) throw new IllegalArgumentException("Dimensions don't match")

  /** @return a string representation of the vector */
  override def toString: String = data.foldLeft("") { (acc, x) => acc + s"$x " }
}
