package model

trait Board:
  def size: Int = fields.length
  def fields: Vector[Field]

  def get(position: Int): Field = fields(relativePosition(position))
  def getAbsolute(position: Int): Option[Field] = fields.lift(position)
  def relativePosition(position: Int): Int = Math.floorMod(position, size)

  def getEvents(oldPosition: Int, distance: Int): Vector[EventField] =
    val passedEvents =
      (0 until distance)
        .map(position => get(oldPosition + position))
        .collect { case event: OnPassField => event }
        .toVector
      
    get(oldPosition + distance) match
      case event: OnEnterField => passedEvents :+ event
      case _ => passedEvents
    

  require(fields.nonEmpty, "A board cannot be empty.")

case class MBoard(fields: Vector[Field]) extends Board
