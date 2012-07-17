package mql.model.parser

import util.parsing.combinator.RegexParsers

trait Utilities extends RegexParsers {
  def surrounded(beginToken: String, endToken: String)(acceptParser: Parser[String]) = {
    beginToken ~> (acceptParser /*| endToken*/ ^^ {
      case v => if (v.last == endToken) v.dropRight(1) else v
    }
  ) <~ endToken
  }

  def freeCharMultiLine = regex("(?s).".r)

  def freeCharSingleLine = regex(".".r)
}

trait IncludeWhiteSpace extends RegexParsers {
  override protected val whiteSpace = "".r
}