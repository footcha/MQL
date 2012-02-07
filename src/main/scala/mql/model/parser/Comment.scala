package mql.model.parser

import util.parsing.combinator.RegexParsers

// TODO 'extends RegexParsers' from object declaration
object Comment extends RegexParsers {
  override val whiteSpace = "".r

  def sqlComment = singleLine("--") | multiLine("/*", "*/")

  def singleLine(separatorToken: String) = separatorToken ~> rep(".".r)

  def multiLine(beginToken: String, endToken: String) = {
    beginToken ~> rep(endToken | "(?s).".r) ^^ {case v => if (v.last == endToken) v.dropRight(1) else v}
  }
}