package com.barrybecker4.math.linear

import org.scalatest.funsuite.AnyFunSuite
import javax.vecmath.Vector2d


class LinearUtilSuite extends AnyFunSuite {

  test("distance") {
    assertResult(3.605551275463989) {
      LinearUtil.distance(new Vector2d(1, 2), new Vector2d(3, 5))
    }
  }
}
