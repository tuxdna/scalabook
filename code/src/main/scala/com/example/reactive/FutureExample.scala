package com.example.reactive

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random
import scala.util.Success
import scala.util.Failure

object FutureExample {
  class NetworkFailure extends RuntimeException
  class InvalidParams extends RuntimeException

  def sleep(duration: Long) { Thread.sleep(duration) }

  def fetchNumberInRange(x: Int, y: Int, chance: Double): Future[Int] = {
    Future {
      val sleepTime = Random.nextInt(500)
      println(s"Wait for ${sleepTime} ms")
      sleep(sleepTime)

      if (Random.nextDouble > 1 - chance) throw new NetworkFailure
      if (x >= y) throw new InvalidParams

      x + Random.nextInt(y - x)
    }
  }

  def main(args: Array[String]) {
    val num1 = fetchNumberInRange(0, 10, 0.5)
    // println(num1)

    num1.onComplete {
      case Success(x) => println(x)
      case Failure(t) => println(t)
    }

    sleep(2000)
//    System.exit(0)

    num1.onFailure {
      case t => println("Failure", t)
    }

    // Await.result(num1, 1000 millis)

    val num2 = fetchNumberInRange(11, 10, 0.01)
    // println(num2)

    num2.recover {
      case _: Throwable => 0
    }

    // Await.result(num2, 1000 millis)

    var sum = 0

    val num3 = fetchNumberInRange(0, 10, 0.01)
    num3.onComplete {
      case Success(x) => sum += x
      case Failure(t) => t
    }

    num2.andThen {
      case Success(x) => sum += x
      case Failure(t) => t
    }

    val lower = List(1, 2, 3, 4, 5, 6, 7, 8)
    val upper = List(2, 4, 5, 12, 15, 16, 20)
    val allFutures = lower.zip(upper).map { lowup =>
      fetchNumberInRange(lowup._1, lowup._2, 0.1)
    }

    val f = allFutures.foldLeft(Future { 0 }) {
      case (future, n) => future map {
        val x = try {
          Await.result(n, 1000 millis)
          n.value.get.get
        } catch {
          case e: Throwable => 0
        }
        _ + x

      }
    }

    val allf2 = lower.zip(upper).map { lowup =>
      fetchNumberInRange(lowup._1, lowup._2, 0.1)
    }

    val f2 = allf2.foldLeft(Future { 0 }) {
      case (future, n) => for(x <- future; y <- n) yield x + y
    }

    f2.onComplete {
      case Success(x) => println("Sequence sum2 = " + x)
      case Failure(t) => println(t)
    }

    Await.result(f2, 10000 millis)
    println("sum = " + sum)
  }
}
