package com.barrybecker4.math.linear

import com.barrybecker4.math.MathUtil
import org.scalactic.{Equality, TolerantNumerics}
import org.scalatest.funsuite.AnyFunSuite

import java.io.ByteArrayOutputStream
import javax.vecmath.{GMatrix, Vector2d}


class LinearUtilSuite extends AnyFunSuite {

  /** Math.sqrt may differ in the last bit across JVMs. */
  implicit val doubleEq: Equality[Double] = TolerantNumerics.tolerantDoubleEquality(MathUtil.EPS_MEDIUM)

  test("distance") {
    assert(3.605551275463989 === LinearUtil.distance(new Vector2d(1, 2), new Vector2d(3, 5)))
  }

  test("printMatrix prints entries rather than entries plus 32") {
    val matrix = new GMatrix(2, 2, Array[Double](1.5, 0, -2.0, 4))
    val captured = new ByteArrayOutputStream
    Console.withOut(captured) {
      LinearUtil.printMatrix(matrix)
    }
    assertResult("1.5   0  \n-2.0 4.0 \n") { captured.toString }
  }
}
