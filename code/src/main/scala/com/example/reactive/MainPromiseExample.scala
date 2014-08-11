package com.example.reactive

import scala.concurrent.Promise

object MainPromiseExample {

  def main(args: Array[String]): Unit = {
    val p = Promise[Int]() //> p  : scala.concurrent.Promise[Int] = scala.concurrent.impl.Promise$DefaultPromise@43c332b8
    p.isCompleted //> res0: Boolean = false
    val f = p.future //> f  : scala.concurrent.Future[Int] = scala.concurrent.impl.Promise$DefaultPromise@43c332b8
    f.isCompleted //> res1: Boolean = false
    p.success(10) //> res2: com.example.reactive.promise.p.type = scala.concurrent.impl.Promise$DefaultPromise@43c332b8

    p.isCompleted //> res3: Boolean = true
    f.isCompleted //> res4: Boolean = true
    // p.success(30) // Error!
    f.value //> res5: Option[scala.util.Try[Int]] = Some(Success(10))

  }

}