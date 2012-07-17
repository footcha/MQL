package mql.model.semantic

import mql.Checker.withNotNull

trait Mapping extends Name with Comment { thisMapping =>
  private var _sourceSystem: SourceSystem = StandardSourceSystem.undefined
  def sourceSystem: SourceSystem = _sourceSystem
  def sourceSystem_=(sourceSystem: SourceSystem) {
    withNotNull(sourceSystem) { _sourceSystem = sourceSystem }
  }

  private var _evaluation: Evaluation = StandardEvaluation.simple
  def evaluation: Evaluation = _evaluation
  def evaluation_=(evaluation: Evaluation) {
    withNotNull(evaluation) { _evaluation = evaluation}
  }

  private var _historization: Historization = Historization.undefined
  def historization: Historization = _historization
  def historization_=(historization: Historization) {
    withNotNull(historization) { _historization  = historization}
  }

  private val _mappingSources = new MappingSources {
    def mapping = thisMapping
  }
  def sources: MappingSources = _mappingSources

  /**
   * Target column mappings
   */
  def columns: ColumnMappings = _columns
  private lazy val _columns = new ColumnMappings {
    def mapping: Mapping = thisMapping
  }

  /**
   * Target entity
   */
  def target: Table
}