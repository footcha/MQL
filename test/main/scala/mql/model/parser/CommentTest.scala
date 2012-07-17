package mql.model.parser

import org.scalatest.FunSuite

class CommentTest extends FunSuite {
  import TestUtilities._

  def validate(result: Comment.ParseResult[_]) {
    import Comment._
    result match {
      case Failure(msg, _) => fail(msg)
      case _ =>
    }
  }

  test("Single line comment") {
    val dsl = "-- test comment "
    val result = Comment.parseAll(Comment.sqlComment, dsl)

    validate(result)
  }

  test("Multi-line comment") {
    val dsl = "/* test " + EOL +
      "  comme*nt */"
    val result = Comment.parseAll(Comment.sqlComment, dsl)

    validate(result)
  }
}