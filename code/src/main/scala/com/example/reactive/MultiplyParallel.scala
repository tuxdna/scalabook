package com.example.reactive

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object MultiplyParallel {

  def multInt(x: Int, y: Int) = x * y

  private def pm[T](l: List[Future[T]])(zero: T)(fn: (T, T) => T): Future[T] = {

    val l1 = l.grouped(2)
    val l2 = l1.map { sl =>
      sl match {
        case x :: Nil => x
        case x :: y :: Nil =>
          for (a <- x; b <- y) yield fn(a, b)
        case _ => Future(zero)
      }
    }.toList

    l2 match {
      case x :: Nil => x
      case x :: xs => pm(l2)(zero)(fn)
      case Nil => Future(zero)
    }
  }

  def parallelAggregate[T](l: List[T])(zero: T)(fn: (T, T) => T): T = {
    val n = pm(l.map(Future(_)))(zero)(fn)
    Await.result(n, 1000 millis)
    n.value.get.get
  }

  def main(args: Array[String]) {

    // multiply empty list: zero value is 1
    println(parallelAggregate(List[Int]())(1)((x, y) => x * y))

    // multiply a list: zero value is 1
    println(parallelAggregate(List(1, 2, 3, 4, 5))(1)((x, y) => x * y))

    // sum a list: zero value is 0
    println(parallelAggregate(List(1, 2, 3, 4, 5))(0)((x, y) => x + y))

    // sum a list: zero value is 0
    val bigList1 = List(1, 2, 3, 4, 5).map(BigInt(_))
    println(parallelAggregate(bigList1)(0)((x, y) => x + y))

    // sum a list of BigInt: zero value is 0
    val bigList2 = (1 to 100).map(BigInt(_)).toList
    println(parallelAggregate(bigList2)(0)((x, y) => x + y))

    // multiply a list of BigInt: zero value is 1
    val bigList3 = (1 to 100).map(BigInt(_)).toList
    println(parallelAggregate(bigList3)(1)((x, y) => x * y))
  }
}

