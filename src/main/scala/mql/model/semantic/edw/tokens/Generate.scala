package mql.model.semantic.edw.tokens

import mql.model.semantic.{SourceSystem, ColumnTransformation}

case class Generate() extends ColumnTransformation

case class SourceSystemTransformation(system: SourceSystem) extends ColumnTransformation
