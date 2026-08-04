package com.barrybecker4.math.linear

import com.barrybecker4.math.MathUtil
import org.scalactic.{Equality, TolerantNumerics}
import org.scalatest.funsuite.AnyFunSuite
import javax.vecmath.Vector2d


class LinearUtilSuite extends AnyFunSuite {

  /** Math.sqrt may differ in the last bit across JVMs. */
  implicit val doubleEq: Equality[Double] = TolerantNumerics.tolerantDoubleEquality(MathUtil.EPS_MEDIUM)

  test("distance") {
    assert(3.605551275463989 === LinearUtil.distance(new Vector2d(1, 2), new Vector2d(3, 5)))
  }
}
