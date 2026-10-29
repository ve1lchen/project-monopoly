
import controller.GameController
import model.{GameState, Player, RandomDice}
import view.ConsoleUI

import scala.io.StdIn

object monopoly:
  def main(args: Array[String]): Unit =

    val players = Vector(Player("Alice", id = 0), Player("Renee", id = 1))
    val controller = GameController(GameState(players = players), new RandomDice)
    val ui = ConsoleUI(controller)
    ui.run()