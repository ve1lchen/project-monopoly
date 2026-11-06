package model

import model.ActionType.*

case class MBoard(fields: Vector[Field]) extends Board:
  override val size: Int = fields.size
  def getField(position: Int): Option[Field] = fields.lift(position)
