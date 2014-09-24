package com.example.reactive
import rx.lang.scala._
import rx.lang.scala.subjects._
// import rx.lang.scala.examples


object MainObservable01 {
  def main(args: Array[String]) {
    val target = Observable.from(List(1, 2, 3, 4))

    val subscription1 = target subscribe (println(_))
    val subscription2 = target subscribe (println(_))

    def addToObservable(toAdd: Int, target: Observable[Int]): Observable[Int] = {
      target /*.addElementAndNotifyObservers(toAdd)*/
    }

    addToObservable(4, target) //should print 4 on all subscriptions
    addToObservable(6, target) //should print 6 on all subscriptions

    println("Correct way")
    val subject2 = ReplaySubject[Int]()
    val initial2 = Observable.from(List(1, 2, 3, 4))
    val target2 = initial2 ++ subject2 // concat the observables

    val subs1 = target2 subscribe (println(_))
    val subs2 = target2 subscribe (println(_))
    println("hi")
    subject2.onNext(5) // emit '4'
    subject2.onNext(6) // emit '6'  
  }

}