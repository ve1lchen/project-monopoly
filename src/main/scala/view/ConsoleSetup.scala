package view

import model.{GameSetup, Player}

import scala.io.StdIn.readLine

object ConsoleSetup:
  def askPlayers: Vector[Player] =
    var result: Option[Vector[Player]] = None

    while result.isEmpty do
      val count = readLine("Enter players count: ").toIntOption
      count match
        case Some(number) if GameSetup.isValidPLayerCount(number) =>
          val names = Vector.tabulate(number) { index =>
            readLine(s"Enter Player ${index + 1} name: ")
          }
          GameSetup.createPlayers(names) match
            case Right(players) =>
              result = Some(players)
            case Left(error) =>
              println(error)

        case _ =>
          println("Enter players count from 2 to 4.")

    result.get
