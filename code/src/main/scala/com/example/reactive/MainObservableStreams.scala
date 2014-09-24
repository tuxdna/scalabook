package com.example.reactive

import rx.lang.scala.subjects.PublishSubject

object MainObservableStreams {

  def main(args: Array[String]) {
    val channel = PublishSubject[Int]

    val a = channel.subscribe(x => println("a: " + x))
    val b = channel.subscribe(x => println("b: " + x))

    channel.onNext(42)
    a.unsubscribe
    channel.onNext(1234)
    channel.onCompleted
    val c = channel.subscribe(x => println("c: " + x))
    channel.onNext(13)
  }
}