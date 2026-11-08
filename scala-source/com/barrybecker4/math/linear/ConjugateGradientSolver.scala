/* Copyright by Barry G. Becker, 2000-2018. Licensed under MIT License: http://www.opensource.org/licenses/MIT */
package com.barrybecker4.math.linear

import javax.vecmath.{GMatrix, GVector}

object ConjugateGradientSolver {
  private val DEFAULT_EPS = 0.000001
}

/**
  * Iterative conjugate-Gradient solver for a system of equations of the form Ax = b
  * Where A is a symmetric, positive definite matrix.
  * See http://en.wikipedia.org/wiki/Conjugate_gradient_method
  * Commonly applied when the matrix is large and sparse.
  * For other sort of matrices, gaussian elimination may work better.
  * @param matrix A in Ax = b
  * @param b      the b vector in Ax = b
  */
class ConjugateGradientSolver(val matrix: GMatrix, val b: GVector) {
  private var eps = ConjugateGradientSolver.DEFAULT_EPS
  /** Classic CG may need up to n iterations for an n×n SPD system; allow a small cushion. */
  private var maxIterations = 2 * matrix.getNumRow

  def setEpsilon(e: Double): Unit = { eps = e }
  def setMaxIterations(num: Int): Unit = { maxIterations = num }

  def solve: GVector = {
    val zeros = new Array[Double](b.getSize)
    solve(new GVector(zeros))
  }

  /** Find a solution or throw if the residual does not fall below [[eps]] within [[maxIterations]].
    * @param initialGuess the initial guess for the solution x, x0
    * @return solution vector
    */
  def solve(initialGuess: GVector): GVector = {
    val x = new GVector(initialGuess)
    val tempv = new GVector(initialGuess)
    tempv.mul(matrix, initialGuess)
    val bb = new GVector(b)
    bb.sub(tempv)
    val r = new GVector(bb)
    val p = new GVector(r)
    val xnew = new GVector(p)
    val rnew = new GVector(p)
    val pnew = new GVector(p)
    val matrixMultp = new GVector(p)
    var error = Math.sqrt(r.dot(r))
    var iteration = 0
    while (error > eps && iteration < maxIterations) {
      matrixMultp.mul(matrix, p)
      val pAp = p.dot(matrixMultp)
      val lambda = r.dot(p) / pAp
      xnew.scaleAdd(lambda, p, x)
      rnew.scaleAdd(-lambda, matrixMultp, r)
      val alpha = -(rnew.dot(matrixMultp) / pAp)
      pnew.scaleAdd(alpha, p, rnew)
      p.set(pnew)
      r.set(rnew)
      x.set(xnew)
      error = Math.sqrt(r.dot(r))
      iteration += 1
    }

    if (error > eps || error.isNaN || error.isInfinite)
      throw new IllegalStateException("Unable to converge on a solution. Error = " + error)
    xnew
  }
}
