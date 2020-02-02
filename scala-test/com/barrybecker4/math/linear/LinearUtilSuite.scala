package com.barrybecker4.math.linear

import javax.vecmath.Vector2d
import org.scalatest.FunSuite


class LinearUtilSuite extends FunSuite {

  test("distance") {
    assertResult(3.605551275463989) {
      LinearUtil.distance(new Vector2d(1, 2), new Vector2d(3, 5))
    }
  }
}
