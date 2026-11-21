package view
import controller.{GameController, Observer}
import controller.command.*
import model.PropertyField

import scala.io.StdIn.readLine

class ConsoleUI(controller: GameController) extends Observer:

  controller.addObserver(this)

  override def update(): Unit =
    render()

  private def exitResults(): Unit =
    val state = controller.getState

    clearScreen()
    println("============= RESULTS ============")
    for player <- state.players do
      println(s" ${player.name} || ${player.money} ||")
      val property = state.propertyMap.collect:
        case (fieldId, ownerId) if ownerId == player.id => fieldId
      println(s"   Properties: ${property.mkString(", ")}")
      println("-------------------------------------")

  private val commands: Map[String, GameCommand] = Map(
    "1" -> RollDiceCommand(controller),
    "2" -> BuyPropertyCommand(controller),
    "3" -> EndTurnCommand(controller)
  )

  def run(): Unit =
    var running = true
    render()

    while running do

      val input = readLine(">> ")

      input match
        case "0" =>
          running = false
          exitResults()
        case _ =>
          commands.get(input) match
            case Some(command) => command.execute()
            case None          => render()

  private def render(): Unit =
    val state        = controller.getState
    val currentField = state.getCurrentField()

    val rollText = state.lastRoll match
      case Some(id, (a, b)) => s"${state.players(id).name}: $a & $b"
      case None             => "-"

    val propertyInfo = currentField match
      case property: PropertyField =>
        state.getCurrentFieldOwner match
          case Some(owner) =>
            s"Owner: ${owner.name}"
          case None =>
            s"Cost: ${property.price}\nField has no owner"
      case _ => ""

    val field =
      s"""
         |======================== MONOPOLY ========================
         |
         |Current turn: ${state.getCurrentPlayer.name}
         |Money: ${state.getCurrentPlayer.money}
         |Last roll: $rollText
         |
         |Current field: ${currentField.name}
         |$propertyInfo
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
