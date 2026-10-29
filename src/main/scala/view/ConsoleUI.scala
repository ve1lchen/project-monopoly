package view
import controller.GameController


class ConsoleUI(controller: GameController):
  def render(): Unit =
    val state = controller.getState
    val currentField = controller.getCurrentField(state.getCurrentPlayer)
    println()
    println("======================== MONOPOLY ========================")
    println()
    println(s"Current turn: ${state.getCurrentPlayer.name}")
    println(s"Money: ${state.getCurrentPlayer.money}")
    println()
    println(s"Current field: ${currentField.name} // ${currentField.cost} // Owner: ???")
    println()


