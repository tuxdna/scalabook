package com.example.reactive
import rx.lang.scala._
import scala.concurrent.duration._

object MainFirstObservable {
  def main(args: Array[String]) {
    val ticks: Observable[Long] = Observable.interval(1 seconds)
    val evens: Observable[Long] = ticks.filter(s => s % 2 == 0)
    val bufs: Observable[Seq[Long]] = evens.buffer(2, 1)
    val s = bufs.subscribe(b => println(b))
    
    val xs = Observable.from(1 to 10)
    val ys = xs.map(x => x + 1)
    ys.subscribe(x => println("HI: "+x))
    
    readLine()
    s.unsubscribe()
  }

}