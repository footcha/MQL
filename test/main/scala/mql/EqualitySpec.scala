/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers._

trait EqualitySpec extends FlatSpec {
  def symmetricObjects(v1: Any, v2: Any, result: Boolean) {
    it should "be symetric" in {
      assert((v1 equals v2) == result, "Value 1 should be equal to Value 2.")
      assert((v2 equals v1) == result, "Value 2 should be equal to Value 1.")
    }
  }

  def transitiveObjects(v1: Any, v2: Any, v3: Any) {
    it should "be transitive" in {
      v1 should equal (v2) //, "Value 1 should be equal to Value 2.")
      v2 should equal (v3)//, "Value 2 should be equal to Value 3.")
      v1 should equal (v3)//, "Value 1 should be equal to Value 3.")
    }
  }

  def basicSymmetry(v1: (Any, Any), v21: Any) {
    val (v11, v12) = v1
    "Same object" should behave like symmetricObjects(v11, v11, result = true)
    "Equal objects (not same object)" should behave like symmetricObjects(v11, v12, result = true)
    "Not equal objects" should behave like symmetricObjects(v11, v21, result = false)
  }
}
