object MyMain {

  object P1 {
    def last[T](l: List[T]) = {
      l.reverse.head
    }

    //    def last(l: List[Int]) = {
    //      l.reverse.head
    //    }
    //
    //    def last(l: List[String]) = {
    //      l.reverse.head
    //    }

    def run = {
      println("Solution for problem 1")
      val l1 = List(1, 1, 2, 3, 5, 8)
      println(last(l1))
      val l2 = List("Aapha", "Beta", "Xeta", "Kappa")
      println(last(l2))

    }
  }

  def main(args: Array[String]) {
    P1.run
  }
}
