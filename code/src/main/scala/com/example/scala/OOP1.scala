package com.example.scala

object OOP1 {

  object MyObject

  trait Animal {
    //    val name: String
    val age: Int = 0
    def introduction: Unit
    def sayHi() = println("Hello, there!")
  }
  
  class Hello

  class Human(var name: String, override val age: Int, gender: String, var position: Int = 0) extends Animal {
    def this() {
      this("", 0, "Male", 0)
      new Hello	
    }
    
    def this(n: String) {
      this(n, 0, "Male", 0)
      new Hello	
    }

    private val address: String = "Somewhere"
    
    def walk(x: Int): Int = {
      position = position + x
      position
    }

    def introduction = println(s"Hi, my name is ${name}!")
    override def sayHi() = {
      super.sayHi
      println("Hello, guys!")
    }
    
    override def toString = s"Human(${name}, ${age}, ${gender}, ${position})"
  }
  
  object Human {
    def apply(n: String) = new Human(n)
    // def unapply()
  }

  def main(args: Array[String]) {

    
    val h1 = new Human("Manny", 16, "Male")
    h1.introduction
    println(" position =  " + h1.position)
    h1.walk(10)
    println(" position =  " + h1.position)
    h1.sayHi

    println(Human)
    
    val h3 = Human("Saleem")
    println(h3)
    
    val h2 = new Human
    
    //    val a = new Object with Animal {
    //      val name = "Tiger"
    //      def introduction = println(s"Hi, my name is ${name}!")
    //    }
    //    a.sayHi
    //    a.introduction

  }

}