package com.example.scala

object FunProg {
  case class Employee(id: Int,
    firstName: String,
    lastName: String,
    designation: String,
    salary: Float,
    department: String)

  def main(args: Array[String]): Unit = {

    val employees = List(
      Employee(10, "Robin", "Verma", "Software Engineer", 130.0f, "Java"),
      Employee(11, "Jacob", "Sharma", "Designer", 111.0f, ".Net"),
      Employee(12, "Paul", "Gupta", "Backend Engineer", 211.0f, "C"),
      Employee(13, "Someone", "Agarwal‎", "Hacker Hacker", 10.0f, "C++"),
      Employee(14, "Max", "Sharma", "Engineer", 160.0f, "Java"),
      Employee(15, "Monaco", "Srivastava‎", "Coder", 210.0f, "Java"),
      Employee(16, "Alex", "Soni‎", "Software Craftsman", 180.0f, "Ruby"),
      Employee(17, "Abby", "Jaiswal", "Systems Engineer", 110.0f, "Scala"),
      Employee(18, "Estillo", "Goel", "Architect", 90.0f, "Python"))

    //      val sum = employees.foldLeft(0.0f)((a,b) => a + b.salary)
    //      val avg = sum / employees.length
    //      
    //      employees.filter(x => x.salary >= avg)

    println("Sorted by ID")
    employees.sortBy(x => x.id) foreach println
    println("Sorted by lastName")
    employees.sortBy(_.lastName) foreach println

    val sum = employees.foldLeft(0.0f)((a, b) => a + b.salary)
    val average = sum / employees.length

    println("Above average:")
    employees.filter(x => x.salary >= average) foreach println

    println("Below average:")
    employees.filter(x => x.salary < average) foreach println

    val (rich, poor) = employees.partition(x => x.salary >= average)

    println("Rich:")
    println(rich)
    println("Poor:")
    println(poor)

    for (e <- employees) {
      e match {
        case Employee(_, _, _, _, _, "Java") => println(e)
        case _ =>
      }
    }
  }
}
