package view
import controller.{GameController, Observer}

import scala.io.StdIn.readLine


class ConsoleUI(controller: GameController) extends Observer:

  controller.addObserver(this)

  override def update(): Unit =
    render()

  def run(): Unit =
    var running = true
    render()

    while running do

      val input = readLine(">> ")

      input match
        case "1" => controller.rollDice()
        case "2" => controller.buyProperty()
        case "3" => controller.endTurn()
        case "0" => {
          running = false
          exitResults()
        }
        case _ => render()


  def render(): Unit =
    val state = controller.getState
    val currentField = controller.getCurrentField(state.getCurrentPlayer)

    val rollText = state.lastRoll match
      case Some(id, (a, b)) => s"${state.players(id).name}: $a & $b"
      case None => "-"

    clearScreen()
    println()
    println("======================== MONOPOLY ========================")
    println()
    println(s"Current turn: ${state.getCurrentPlayer.name}")
    println(s"Money: ${state.getCurrentPlayer.money}")
    println(s"Last roll: ${rollText}")
    println()
    println(s"Current field: ${currentField.name}")
    println(s"Cost: ${currentField.cost}")
    println(s"${controller.getCurrentFieldOwner match
      case Some(player) => s"Owner: ${player.name}"
      case None => "Field has no owner"}")
    println()
    println("[1] Roll")
    println("[2] Buy")
    println("[3] End turn")
    println()
    println("[0] Exit")

  private def clearScreen(): Unit =
    print("\u001b[H\u001b[2J\u001b[3J")
    Console.flush()

  private def exitResults(): Unit =
    val state = controller.getState

    clearScreen()
    println("============= RESULTS ============")
    for (player <- state.players) {
      println(s" ${player.name} || ${player.money} ||")
      val property = state.propertyMap.collect {
        case (field, ownerId) if ownerId == player.id => field
      }
      println(s"   Properties: ${property.mkString(", ")}")
      println("-------------------------------------")
    }

