package com.example.scala

object OOP2 {

  trait LivingBeing

  trait Animal {
    def name: String
    def lifespan: Int
  }

  case class Position(lat: Int, lon: Int)

  trait Human {
    def name: String
    def age: Int
    def gender: String
    def walk(x: Int, angle: Int): Position
  }

  class Employee(
    var id: Int,
    var firstName: String,
    var lastName: String,
    var designation: String,
    var salary: Float,
    var department: String)

  def main(args: Array[String]) {

    val e1 = new Employee(10, "Sam", "Anderson", "Software Engineer", 130.0f, "Java")
    e1.firstName
    

  }

}