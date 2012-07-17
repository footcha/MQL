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
  //    private val x = idMetadata
  //    idMetadata = {
  //      () => {
  //        val xx = x()
  //        xx += Column.name(null, "elem")
  //        xx
  //      }
  //    }
  //  }