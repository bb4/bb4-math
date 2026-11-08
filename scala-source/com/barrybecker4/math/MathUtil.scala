/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math

import java.math.BigInteger

import javax.vecmath.Point2d

import scala.annotation.tailrec
import scala.util.Random


/**
  * Some supplemental mathematics routines.
  */
object MathUtil {
  /** Java double precision cannot accurately represent more than 16 decimal places */
  val EPS = 0.0000000000000001
  val EPS_MEDIUM = 0.0000000001
  val EPS_BIG = 0.0001

  /**
    * Deprecated shared mutable [[Random]] seeded with `1` for reproducible sequences across callers.
    *
    * Not thread-safe: concurrent use from multiple threads can corrupt the generator state.
    * Prefer constructing a local `new Random(...)` when you need isolation, a different seed,
    * or concurrent access. Callers that intentionally share this instance (for example to
    * reseed via `setSeed` for reproducible benchmarks) should serialize access themselves.
    */
  @deprecated(
    "Shared mutable fixed-seed Random; not thread-safe. Construct your own Random when you need isolation, a different seed, or concurrent access.",
    since = "2.1"
  )
  val RANDOM = new Random(1)

  /** @param v the value to find log base 10 of.
    * @return log base 10 of the specified value.
    */
  def log10(v: Double): Double = Math.log10(v)

  /** @param v the value to find 10 to the power of
    * @return 10 to the val power.
    */
  def exp10(v: Double): Double = Math.pow(10.0, v)

  /**
    * Greatest common divisor of 2 longs (may be negative).
    * Deliberately returns `1L` for `gcd(0, 0)` (often left undefined or defined as 0 elsewhere).
    */
  @tailrec
  def gcd(a: Long, b: Long): Long =
    if (a == 0 && b == 0) 1L
    else if (a < 0) gcd(-a, b)
    else if (b < 0) gcd(a, -b)
    else if (a == 0) b
    else if (b == 0) a
    else if (a == b) a
    else if (b < a) gcd(b, a)
    else gcd(a, b % a)

  /**
    * Least common multiple of a and b.
    * Divides by the GCD before multiplying so intermediate `a * b` cannot overflow a Long
    * when the mathematical LCM itself still fits. If the true LCM exceeds `Long.MaxValue`,
    * the result may still wrap.
    * @param a numerator
    * @param b denominator
    * @return the least common multiple of a and b
    */
  def lcm(a: Long, b: Long): Long = {
    val g = gcd(a, b)
    Math.abs(a / g * b)
  }

  /** Find the least common multiple of specified values.
    * @param values values to find least common multiple of
    * @return the least common multiple of values[0], values[1],... values[i].
    */
  def lcm(values: Array[Int]): Long =
    values.foldLeft(1L)((acc, v) => lcm(acc, v))

  /** Implementation of c(m,n)
    * @param m total number of items
    * @param n number of items to choose where order does not matter
    * @return number of combinations
    */
  def combination(m: Int, n: Int): BigInteger = {
    val diff = m - n
    val (numerator, denominator) =
      if (n > diff) (bigPermutation(m, n), bigFactorial(diff))
      else (bigPermutation(m, diff), bigFactorial(n))
    numerator.divide(denominator)
  }

  /**
    * Factorial for values that fit in a signed 64-bit Long.
    * 0! = 1 (http://www.zero-factorial.com/whatis.html).
    * Safe only for `0` through `20` (`20!` = 2432902008176640000L is the largest that fits).
    * For larger arguments use [[bigFactorial]]; `factorial(21)` and above throw rather than
    * silently overflowing to a negative Long.
    * @param num number to take factorial of (`0`..`20`)
    * @return num!
    * @throws IllegalArgumentException if num &gt; 20
    */
  def factorial(num: Int): Long = {
    assert(num >= 0)
    if (num > 20)
      throw new IllegalArgumentException(
        s"factorial($num) overflows Long; use bigFactorial for n > 20")
    if (num == 0) return 1
    var ct = num
    var f: Long = 1
    while (ct > 1) {
      f *= ct
      ct -= 1
    }
    f
  }

  def bigFactorial(num: Int): BigInteger = {
    assert(num >= 0)
    if (num == 0) return BigInteger.ONE
    var ct = new BigInteger(Integer.toString(num))
    var f = BigInteger.ONE
    while (ct.compareTo(BigInteger.ONE) > 0) {
      f = f.multiply(ct)
      ct = ct.subtract(BigInteger.ONE)
    }
    f
  }

  /** Permutation function computes a!/b!.
    * 0! = 1 (http://www.zero-factorial.com/whatis.html)
    * @param a number of items to select from
    * @param b number of items to select order being important.
    * @return a!/b!
    */
  def permutation(a: Int, b: Int): Long = {
    assert(a > 0)
    if (a == b) 1L
    else {
      assert(a > b)
      var f = a
      var anew = a - 1
      while (anew > b) {
        f *= anew
        anew -= 1
      }
      f
    }
  }

  /** Permutation function computes a!/b!.
    * 0! = 1 (http://www.zero-factorial.com/whatis.html)
    * @param a number of items to select from
    * @param b number of items to select, order being important.
    * @return a!/b!
    */
  def bigPermutation(a: Int, b: Int): BigInteger = {
    if (a == 0) throw new IllegalArgumentException("The number of items to select from was 0")
    if (a < b) throw new IllegalArgumentException("The number of items to select (" + b + ") must be >= " + a)
    var f = new BigInteger(Integer.toString(a))
    if (a == b) BigInteger.ONE
    else {
      var anew = a - 1
      //BigInteger anew = new BigInteger(a-1);
      while (anew > b) {
        f = f.multiply(new BigInteger(Integer.toString(anew)))
        anew -= 1
      }
      f
    }
  }

  def getDirectionTo(fromPoint: Point2d, toPoint: Point2d): Double = {
    val xDiff = toPoint.x - fromPoint.x
    val yDiff = toPoint.y - fromPoint.y
    Math.atan2(yDiff, xDiff)
  }
}
