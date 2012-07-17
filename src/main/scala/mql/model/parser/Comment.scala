package mql.model.parser

import util.parsing.combinator.RegexParsers

// TODO 'extends RegexParsers' from object declaration
object Comment extends RegexParsers with IncludeWhiteSpace with Utilities {
  def sqlComment = singleLine("--") | surrounded("/*", "*/") {
    freeCharMultiLine
  }

  def singleLine(separatorToken: String) = separatorToken ~> rep(freeCharSingleLine)
}