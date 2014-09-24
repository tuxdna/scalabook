package com.example.reactive

import rx.lang.scala.Observable
import rx.lang.scala.Subscription
import rx.lang.scala.Observer
import rx.lang.scala.subscriptions.CompositeSubscription
import rx.lang.scala.subscriptions.MultipleAssignmentSubscription
import rx.lang.scala.subjects.PublishSubject

object MainObservableCreate {
  def calculateElement(index: Int): String = {
    println("omg I'm calculating so hard")
    index match {
      case 0 => "a"
      case 1 => "b"
      case _ => throw new IllegalArgumentException
    }
  }

  def main(args: Array[String]) {

    val my1 = Observable.from(List(1, 2, 3, 4, 5))

    val obs1 = Observable.from((1 to 10))

    val subs1 = obs1.subscribe({ x => print(" "+x) }, { t => println(t) }, { () => println(" Completed") })

    // concatenate
    val c1 = my1 ++ obs1
    
    println("Another")
    val ss1 = c1.subscribe(x => print(" "+x), y => println(y), () => println(" Done"))
    ss1.unsubscribe

    // subs1
    
    println("Take 1")

    // use create method
    val o1 = Observable.create[String](observer => {
      observer.onNext(calculateElement(0))
      observer.onNext(calculateElement(1))
      observer.onCompleted()
      Subscription {}
    })
    o1.take(1).subscribe(println(_))

    println("Take 2")

    // use apply method
    val o2 = Observable[String](subscriber => {
      var i = 0
      while (i < 2 && !subscriber.isUnsubscribed) {
        subscriber.onNext(calculateElement(i))
        i += 1
      }
      if (!subscriber.isUnsubscribed) subscriber.onCompleted()
    })

    o2.take(1).subscribe(println(_))

    println("Take 3")
    o2.subscribe(println(_))

  }

}