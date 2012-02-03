package mql.model.semantic

object System extends System

class System /*extends Parameter with Parameters*/ {

  object Language extends Language

  class Language /*extends Parameter with Parameters*/ {
    var version = new Version
  }

  class Version /*extends Parameter with Parameters*/ {
    def major: Int = 0

    def minor: Int = 0
  }

  var language = Language

  //  var checksum = Checksum
}

class Checksum //extends Parameter