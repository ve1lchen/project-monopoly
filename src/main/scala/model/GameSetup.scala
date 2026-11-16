package model

object GameSetup:
  def isValidPLayerCount(count: Int): Boolean =
    count >= 2 && count <= 4

  def createPlayers(names: Vector[String]): Either[String, Vector[Player]] =
    if !isValidPLayerCount(names.size) then
      Left("Players count must be from 2 to 4.")
    else if names.exists(_.trim.isEmpty) then
      Left("Player names must not be empty.")
    else
      Right(
        names.zipWithIndex.map { case (name, id) =>
          Player(name = name, id = id)
        }
      )