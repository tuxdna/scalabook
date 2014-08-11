package com.example.reactive
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Promise
import scala.util.Success
import scala.util.Failure

object MainFutureFold {

  def main(args: Array[String]): Unit = {

    val intList = List(1, 2, 3) //> intList  : List[Int] = List(1, 2, 3)

    val wrongway = intList.foldLeft(Future { 0 }) {
      case (future, i) => future andThen {
        case Success(result) => result + i
        case Failure(e) => println(e)
      }
    }

    wrongway onSuccess { case x => println(x) }

    intList.foldLeft(Future { 0 }) {
      case (future, i) => future map { _ + i }
    } onSuccess { case x => println(x) }

    Thread.sleep(2000)
  }

}