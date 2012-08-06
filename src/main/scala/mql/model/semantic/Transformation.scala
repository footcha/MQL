/**
 * Copyright (c) 2012 Petr Kozelek <petr.kozelek@gmail.com>
 *
 * The full copyright and license information is presented
 * in the file LICENSE that was distributed with this source code.
 */
package mql.model.semantic

trait Transformation {
  def mappings: Iterable[Mapping]
}
//  val mapping = new KeyMapping {
//    def target = null
//
//    val sourceSystem = _
//    var sourceIdMetadata = _
//    val prefix = _
//  }

//  new KeyMapping {
//    def target = new Table("X")
//
//    val sourceSystem = () => StandardSourceSystem.man
//    val prefix = () => "PFX"
//  }

  //  new KeyMapping {
  //    val sourceSystem = _
  //    val id = _
  //    val prefix = _
  //
  //    private val createModelItems = idMetadata
  //    idMetadata = {
  //      () => {
  //        val xx = createModelItems()
  //        xx += Column.name(null, "elem")
  //        xx
  //      }
  //    }
  //  }