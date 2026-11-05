package model

trait Board:
  def size: Int
  def getField(position: Int): Field