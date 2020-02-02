package com.barrybecker4.math.function

import org.scalactic.{Equality, TolerantNumerics}
import org.scalatest.FunSuite

trait BaseFunctionSuite extends FunSuite {

  implicit val doubleEq: Equality[Double] = TolerantNumerics.tolerantDoubleEquality(0.000001)

  protected def verify(exp: Array[Double], act: Array[Double]): Unit = {
    exp.zip(act).foreach(e => assert(e._1 === e._2))
  }
}
