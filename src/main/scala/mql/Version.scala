package mql

import java.text.SimpleDateFormat

object Version {
  val version = "1.0.55"
  private val releaseDate = "20120807165624"
  val releasedAt = new SimpleDateFormat("yyyyMMddHHmmss").parse(releaseDate)
}