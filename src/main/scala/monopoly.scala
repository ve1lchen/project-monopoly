import controller.GameController
import model.{GameState, Player, RandomDice}
import view.{ConsoleSetup, ConsoleUI}

object monopoly:
  def main(args: Array[String]): Unit =

    val players    = ConsoleSetup.askPlayers
    val controller =
      GameController(GameState(players = players), new RandomDice)
    val ui = ConsoleUI(controller)
    ui.run()
