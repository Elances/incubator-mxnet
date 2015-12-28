package ml.dmlc.mxnet

import ml.dmlc.mxnet.NDArrayConversions._
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import org.scalactic.Tolerance._

class NDArraySuite extends FunSuite with BeforeAndAfterAll {
  test("to java array") {
    val ndarray = NDArray.zeros(2, 2)
    assert(ndarray.toArray === Array(0f, 0f, 0f, 0f))
  }

  test("to scalar") {
    val ndzeros = NDArray.zeros(1)
    assert(ndzeros.toScalar === 0f)
    val ndones = NDArray.ones(1)
    assert(ndones.toScalar === 1f)
  }

  test ("call toScalar on an ndarray which is not a scalar") {
    intercept[Exception] { NDArray.zeros(1, 1).toScalar }
  }

  test("size and shape") {
    val ndzeros = NDArray.zeros(4, 1)
    assert(ndzeros.shape === Array(4, 1))
    assert(ndzeros.size === 4)
  }

  test("set scalar value") {
    val ndarray = NDArray.empty(2, 1)
    ndarray.set(10f)
    assert(ndarray.toArray === Array(10f, 10f))
  }

  test("copy from java array") {
    val ndarray = NDArray.empty(4, 1)
    ndarray.set(Array(1f, 2f, 3f, 4f))
    assert(ndarray.toArray === Array(1f, 2f, 3f, 4f))
  }

  test("plus") {
    val ndzeros = NDArray.zeros(2, 1)
    val ndones = ndzeros + 1f
    assert(ndones.toArray === Array(1f, 1f))
    assert((ndones + ndzeros).toArray === Array(1f, 1f))
    assert((1 + ndones).toArray === Array(2f, 2f))
    // in-place
    ndones += ndones
    assert(ndones.toArray === Array(2f, 2f))
  }

  test("minus") {
    val ndones = NDArray.ones(2, 1)
    val ndzeros = ndones - 1f
    assert(ndzeros.toArray === Array(0f, 0f))
    assert((ndones - ndzeros).toArray === Array(1f, 1f))
    assert((ndzeros - ndones).toArray === Array(-1f, -1f))
    assert((ndones - 1).toArray === Array(0f, 0f))
    // in-place
    ndones -= ndones
    assert(ndones.toArray === Array(0f, 0f))
  }

  test("multiplication") {
    val ndones = NDArray.ones(2, 1)
    val ndtwos = ndones * 2
    assert(ndtwos.toArray === Array(2f, 2f))
    assert((ndones * ndones).toArray === Array(1f, 1f))
    assert((ndtwos * ndtwos).toArray === Array(4f, 4f))
    ndtwos *= ndtwos
    // in-place
    assert(ndtwos.toArray === Array(4f, 4f))
  }

  test("division") {
    val ndones = NDArray.ones(2, 1)
    val ndzeros = ndones - 1f
    val ndhalves = ndones / 2
    assert(ndhalves.toArray === Array(0.5f, 0.5f))
    assert((ndhalves / ndhalves).toArray === Array(1f, 1f))
    assert((ndones / ndones).toArray === Array(1f, 1f))
    assert((ndzeros / ndones).toArray === Array(0f, 0f))
    ndhalves /= ndhalves
    // in-place
    assert(ndhalves.toArray === Array(1f, 1f))
  }

  test("clip") {
    val ndarray = NDArray.empty(3, 2)
    ndarray.set(Array(1f, 2f, 3f, 4f, 5f, 6f))
    assert(NDArray.clip(ndarray, 2f, 5f).toArray === Array(2f, 2f, 3f, 4f, 5f, 5f))
  }

  test("sqrt") {
    val ndarray = NDArray.empty(4, 1)
    ndarray.set(Array(0f, 1f, 4f, 9f))
    assert(NDArray.sqrt(ndarray).toArray === Array(0f, 1f, 2f, 3f))
  }

  test("norm") {
    val ndarray = NDArray.empty(3, 1)
    ndarray.set(Array(1f, 2f, 3f))
    val normed = NDArray.norm(ndarray)
    assert(normed.shape === Array(1))
    assert(normed.toScalar === math.sqrt(14.0).toFloat +- 1e-3f)
  }
}
