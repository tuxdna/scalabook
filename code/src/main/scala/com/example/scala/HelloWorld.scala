package com.example.scala

object HelloWorld extends App {

  def say() = {
    println("Something I said here.")
  }

  def add(x: Int, y: Int) = {
    def sum(x: Int, y: Int) = x + y

    val z = sum(_, _)
    z
  }

  def sum(x: Int, y: Int) = {
    10
    230
    "asdfasd"

    x + y
  }

  def sixplus(x: Int) = 6 + x

  val sixPlus = sum(6, _: Int)

  println(sixPlus(10))

  // val say = "How do you do?"

  println("Hello Scala World")

  println(sum(10, 20))

}
