package mql.transformation

import mql.model.semantic.Node

class InvalidNodeException(val node: Node) extends Exception