package view
import controller.GameController

import scala.io.StdIn.readLine


class ConsoleUI(controller: GameController):

  def run(): Unit =
    var running = true

    while running do
      render()
      val input = readLine(">> ")

      input match
        case "1" => controller.rollDice()
        case "2" => controller.buyProperty()
        case "3" => controller.endTurn()
        case "4" => running = false
        case _ => render()



  def render(): Unit =
    val state = controller.getState
    val currentField = controller.getCurrentField(state.getCurrentPlayer)
    clearScreen()
    println()
    println("======================== MONOPOLY ========================")
    println()
    println(s"Current turn: ${state.getCurrentPlayer.name}")
    println(s"Money: ${state.getCurrentPlayer.money}")
    println(s"Last rolle: ${state.getCurrentPlayer.name} : ${state.lastRoll._1} & ${state.lastRoll._2}")
    println()
    println(s"Current field: ${currentField.name}")
    println(s"Cost: ${currentField.cost}")
    println(s"${controller.getCurrentFieldOwner match
      case Some(player) => "Owner: ${player.name}"
      case None => "Field has no owner"}")
    println()
    println("[1] Roll")
    println("[2] Buy")
    println("[3] End turn")
    println()
    println("[0] Exit")

  private def clearScreen(): Unit =
    print("\u001b[2J\u001b[H")
    print("\u001b[H\u001b[2J\u001b[3J")
    Console.flush()


