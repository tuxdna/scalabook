package com.example.scala

object Driver2 extends App {

  class Employee(id: Int,
    name: String) {

    override def toString() = {
      s"Employee(${id}, ${name})"
    }
  }

  object Employee {
    def apply(id: Int, name: String) = new Employee(id, name)
  }

  val employees = List(
    new Employee(10, "Saleem"),
    new Employee(11, "Shalini"),
    new Employee(12, "Varun"),
    Employee(13, "Vadi"))

  println(employees)

}