
import controller.GameController
import model.{GameState, Player, RandomDice}
import view.ConsoleUI

import scala.io.StdIn

object monopoly:
  def main(args: Array[String]): Unit =

    val players = Vector(Player("Alice"), Player("Renee"))
    val controller = GameController(GameState(players = players), new RandomDice)
    val ui = ConsoleUI(controller)
    ui.render()
    controller.playTurn()
    ui.render()
    controller.playTurn()
    ui.render()
