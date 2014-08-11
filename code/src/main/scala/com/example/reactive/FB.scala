package com.example.reactive
import scala.concurrent.Promise
import scala.util.Success
import scala.util.Failure
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object FB {
  class Page
  class Post
  class Like
  class Comment
  case class Statistics(likes: Seq[Like], comments: Seq[Comment])

  trait IFacebookService {
    def postsFrom(page: Page): Future[Seq[Post]]
    def likesOn(post: Post): Future[Seq[Like]]
    def commentsOn(post: Post): Future[Seq[Comment]]
  }

  object FacebookService extends IFacebookService {
    def postsFrom(page: Page): Future[Seq[Post]] = { Future { List[Post]() } }
    def likesOn(post: Post): Future[Seq[Like]] = { Future { List[Like]() } }
    def commentsOn(post: Post): Future[Seq[Comment]] = { Future { List[Comment]() } }
  }

  def getStatistics4(page: Page): Future[Statistics] = {
    val postsFuture = FacebookService.postsFrom(page)

    val stats = postsFuture.flatMap { posts =>
      val partialStatsFuture = posts.map { post =>
        val fl = FacebookService.likesOn(post)
        val fc = FacebookService.commentsOn(post)

        for (l <- fl; c <- fc) yield (Statistics(l, c))
      }
      Future.sequence(partialStatsFuture).map { pstats =>
        pstats.fold(Statistics(Seq(), Seq())) { (a, b) =>
          Statistics(a.likes ++ b.likes, a.comments ++ b.comments)
        }

      }
    }
    stats
  }

  def getStatistics3(page: Page): Future[Statistics] = {
    val postsFuture = FacebookService.postsFrom(page)

    val stats = postsFuture.flatMap { posts =>
      val ls = posts.map { post => FacebookService.likesOn(post) }
      val postLikes = Future.sequence(ls).map(_.flatten)

      val cs = posts.map { post => FacebookService.commentsOn(post) }
      val postComments = Future.sequence(cs).map(_.flatten)

      for (likes <- postLikes; comments <- postComments) yield Statistics(likes, comments)
    }
    stats
  }

  def getStatistics2(page: Page): Future[Statistics] = {
    val postsFuture = FacebookService.postsFrom(page)
    val x = postsFuture.flatMap { posts =>
      val ls = for (post <- posts) yield { FacebookService.likesOn(post) }
      val postLikes = Future.sequence(ls).map(_.flatten)

      val cs = for (post <- posts) yield FacebookService.commentsOn(post)
      val postComments = Future.sequence(cs).map(_.flatten)

      for (lks <- postLikes; cts <- postComments) yield Statistics(lks, cts)
    }
    x
  }

  def getStatistics1(page: Page): Statistics = {
    val postsFuture = FacebookService.postsFrom(page)
    var likes = Seq.empty[Like]
    var comments = Seq.empty[Comment]
    postsFuture.onComplete {
      case Success(posts) =>
        for (post <- posts) {
          val likesFuture = FacebookService.likesOn(post)
          likesFuture.onComplete {
            case Success(ls) =>
              likes ++= ls
            case Failure(t) =>
          }
          val commentsFuture = FacebookService.commentsOn(post)
          commentsFuture.onComplete {
            case Success(cs) =>
              comments ++= cs
            case Failure(t) =>
          }

          likesFuture.value
          commentsFuture.value
        }
      case Failure(t) =>
    }

    new Statistics(likes, comments)
  }
}