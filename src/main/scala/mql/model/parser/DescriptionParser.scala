package mql.model.parser

import util.parsing.combinator.JavaTokenParsers
import util.parsing.combinator.RegexParsers
import util.parsing.combinator.syntactical.StandardTokenParsers
import util.matching.Regex
import java.util.regex.Pattern

class DescriptionParser extends StandardTokenParsers  {

  //val regex = new RegexParsers {}

  def description = keyword("Description") ~> operator ~> stringLit

  def operator: Parser[String] = keyword("=")

  //def value: Parser[String] = regex.regex(new Regex(".*", null))
}