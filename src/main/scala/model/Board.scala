package model

trait Board:
  def size: Int
  def fields: Vector[Field]
  def getField(position: Int): Option[Field]
