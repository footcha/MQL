package mql.model.parser

import scala.util.parsing.combinator._
import util.parsing.input.StreamReader

class JSON1 extends JavaTokenParsers {

  def obj: Parser[Map[String, Any]] =
    "{" ~> repsep(member, ",") <~ "}" ^^ (Map() ++ _)

  def arr: Parser[List[Any]] =
    "[" ~> repsep(value, ",") <~ "]"

  def member: Parser[(String, Any)] =
    stringLiteral ~ ":" ~ value ^^ {
      case name ~ ":" ~ value => (name, value)
    }

  def value: Parser[Any] = (
    obj
      | arr
      | stringLiteral
      | floatingPointNumber ^^ (_.toDouble)
      | "null" ^^ (x => null)
      | "true" ^^ (x => true)
      | "false" ^^ (x => false)
    )
}

object JSON1Test extends JSON1 {
  def main(args: Array[String]) {
    val reader = StreamReader(new java.io.FileReader(args(0)))
    //val tokens = new lexical.Scanner(reader)
    //    println(phrase(value)(tokens))
    println(parseAll(value, reader))
  }
}

trait ParserBase extends Utilities

trait Metadata extends ParserBase with IncludeWhiteSpace {

  def metadata() = operator ~> WS ~> ((multiLine | singleLine))

  //private def freeMetadata() = operator2 ~> whiteSpace2 ~ ((multiLine | singleLine) ^^ action)
  //  private def operator2 = literal("-->")

  def operator = literal("=>")

  //def action: List[_] => List[_] = x => x

  private val WS = regex("""\s*""".r)

  private def multiLine = surrounded("{", "}") {
    freeCharMultiLine
  }

  private def singleLine = rep(freeCharSingleLine)
}

trait Variable extends ParserBase with IncludeWhiteSpace {

  def name: Parser[String] // = ("(?ii)" + name).r

  def operator = literal("=")

  def value = (
    multiLine
      | singleLine
    ) ^^ {case x => x.toString}

  private def multiLine = surrounded("{", "}") {
    (regex("(?s)[^\\}]*".r) //^^ {
      //      case x => {
      //        //printf("freeCharMultiLine: %s\n", x.mkString)
      //        x
      //      }
      /*}*/)
  }

  private def singleLine = rep(freeCharSingleLine)

  private def metadataOperator = literal("=>") //~> err("Not supported")

  def metadataForValue2 = new Parser[Any] {
    def apply(in: Input) = {
      val m = new Metadata {}
      m.metadata().apply(in) match {
        case m.Success(v, n) => Success(v, n)
        case m.Error(msg, n) => Error(msg, n)
        case m.Failure(msg, n) => Failure(msg, n)
      }
    }
  } //^^ {case x => printf("meta: %s\n", x.mkString)}

  def metadataForValue = metadata
//  def metadataForValue = Parser(
//    in => {
//      val m = new Metadata {}
//      m.metadata().apply(in) match {
//        case m.Success(v, n) => Success(v, n)
//        case m.Error(msg, n) => Error(msg, n)
//        case m.Failure(msg, n) => Failure(msg, n)
//      }
//
//      //      Failure(",", in)
//    }
//  )

//  val withWhiteSpacesParser = new Variable with IncludeWhiteSpace {
//    def name: this.type#Parser[String] = null
//  }

  def metadata = (
    (literal("=>") ^^ (Operator(_)))
      ~> WS
      ~> ((multiLine2 | singleLine2) ^^ {case x:List[_] => Value(x.mkString)})
    )

  private def multiLine2 = surrounded("{", "}") {
    freeCharMultiLine
  }

  private def singleLine2 = rep(freeCharSingleLine)

  def metadataForLabel = metadataOperator // TODO

  private val WS = regex("""\s*""".r)

  def apply() = (
    (name ^^ {
      case x => Var(x)
    })
      ~ WS
      ~ (operator ^^ (Operator(_)))
      ~ WS
      ~ (value ^^ (Value(_)))
      ~ WS
      ~ (metadataForValue  ?)
    )

  //def log(label: String) = {case x => printf(label + ": %s\n", x)}
}

case class Token() {
  def log(text: String) {
    printf("[%s] %s\n", this.getClass.getName, text)
  }
}

case class Var(name: String) extends Token {
  log(name)
}

case class Operator(name: String) extends Token {
  log(name)
}

case class Value(value: String) extends Token {
  log(value)
}