package com.example.reactive

import scala.util.Try
import sun.io.MalformedInputException
import scala.util.Success
import scala.util.Failure

object TryExample {

  class OutOfRangeException extends Throwable

  def inputRangeOneToFive = {
    println("Enter an integer in range [1, 5]: ")
    val n = Console.readInt
    n match {
      case x if (1 to 5) contains n => x
      case _ => throw new OutOfRangeException; n
    }
  }

  def main(args: Array[String]): Unit = {

    // Usual approach
    val n1 = inputRangeOneToFive
    println(s"You entered ${n1}")

    // Add error handling
    try {
      val a = inputRangeOneToFive
      println(a)
    } catch {
      case e1: OutOfRangeException => println(e1)
      case e2: NumberFormatException => println(e2)
    }

    // Using Try
    val n2 = Try(inputRangeOneToFive)
    n2 match {
      case Success(x) => println(x)
      case Failure(t) => println(t)
    }

    println(s"You entered ${n2}")

    // recover on failure
    val n3 = n2.recoverWith {
      case x: OutOfRangeException => Success(0)
    }
    
    val mx = n3.map( x => x * x)

    println(s"Recovery ${n3}")

    // simplify failures!
    val rv = for (
      x <- Try(inputRangeOneToFive).recover{
        case e: Exception => 3
      };
      y <- Try(inputRangeOneToFive)
    ) yield x + y

    
    println(s"Result is : ${rv}")
  }
}
