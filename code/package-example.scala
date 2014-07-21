package in.tuxdna.scala
class Employee(name: String, age: Int) {
  override def toString = name + ", " + age
}

object Main extends App {
  val emp1 = new Employee("Tom", 21)
  println("Employee 1: "+emp1)
}
