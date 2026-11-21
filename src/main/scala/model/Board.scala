package model

trait Board:
  def size: Int = fields.length
  def fields: Vector[Field]
  def get(position: Int): Field = fields(relativePosition(position))
  def getAbsolute(position: Int): Option[Field] = fields.lift(position)
  def relativePosition(position: Int): Int = Math.floorMod(position, size)
  
  require(fields.nonEmpty, "A board cannot be empty.")

case class MBoard(fields: Vector[Field]) extends Board
