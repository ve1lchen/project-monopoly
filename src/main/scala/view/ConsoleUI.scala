package view
import controller.{GameController, Observer}
import model.Player

import scala.io.StdIn.readLine

object ConsoleSetup:
  def askPlayers: Vector[Player] =
    var valid = false
    var players: Vector[Player] = Vector.empty

    while !valid do
      val count = readLine("Enter players count: ").toIntOption
      count match
        case Some(count) =>
          if count >= 2 && count <= 4 then
            valid = true

            for (i <- 0 until count) do
              val name = readLine(s"Enter Player ${i+1} name: ")
              players = players :+ Player(name = name, id = i)
        case _ =>
          println("Enter players count from 2 to 4.")
    players

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

    val field =
      s"""
         |======================== MONOPOLY ========================
         |
         |Current turn: ${state.getCurrentPlayer.name}
         |Money: ${state.getCurrentPlayer.money}
         |Last roll: ${rollText}
         |
         |Current field: ${currentField.name}
         |Cost: ${currentField.cost}
         |${controller.getCurrentFieldOwner match
          case Some(player) => s"Owner: ${player.name}"
          case None => "Field has no owner"}
         |
         |[1] Roll
         |[2] Buy
         |[3] End turn
         |
         |[0] Exit
         |""".stripMargin

    clearScreen()
    print(field)

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
