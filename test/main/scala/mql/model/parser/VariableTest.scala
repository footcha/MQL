package mql.model.parser

import org.scalatest.FunSuite

class VariableTest extends FunSuite {

  import TestUtilities._

  object TestParser extends Variable {
    override def name = "(?ii)Historization".r
  }

  def validate(result: TestParser.ParseResult[_]) {
    import TestParser._
    result match {
      case Failure(msg, _) => fail(msg)
      case _ =>
    }
  }

  def parse(parser: TestParser.Parser[_])(dsl: => String) {
    val result = TestParser.parseAll(parser, dsl)
    validate(result)
    result
  }

  test("metadataForValue - single line") {
    val metadata = "this is my metadata"
    val dsl = "=>    \r\n " + metadata
    var txt = ""

    parse(TestParser.metadataForValue ^^ {
      case x => txt += x
    }) {
      dsl
    }

    expect(metadata) {
      txt.toString
    }
  }

  test("metadataForValue - multi-line") {
    val metadata = "this is my " + EOL +
      "long metadata"
    val dsl = "=>     \r\n " + "{" + metadata + "}"
    var txt = ""

    parse(TestParser.metadataForValue ^^ {
      case x => txt += x
    }) {
      dsl
    }

    expect(metadata) {
      txt.toString
    }
  }

  test("variable name") {
    val parser = TestParser.name;
    parse(parser) {
      "Historization"
    }
    parse(parser) {
      "historizatioN"
    }
  }

//  test("JSON") {
//    var json = """{
//    "address book": {
//      "name": "John Smith",
//      "address": {
//        "street": "10 Market Street",
//        "city"  : "San Francisco, CA",
//        "zip"   : 94111
//      },
//      "phone numbers": [
//        "408 338-4238",
//        "408 111-6892"
//      ]
//    }
//  }""";
//    var result = JSON1Test.parseAll(JSON1Test.value, json);
//    val res = result.get
//    println(result)
//  }

  test("variable") {
    val parser = TestParser()
    val result = parse(parser) {
      "Historization =  { this is very " + EOL + " long value }      " +
        EOL +
        "  =>        Description = description value"
    }
    print(result)
  }
}