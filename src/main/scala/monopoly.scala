
import controller.GameController
import model.{GameState, Player, RandomDice}

import scala.io.StdIn

object monopoly:
  def main(args: Array[String]): Unit =

//    print("Enter player`s count: ")
//    val playersCount = StdIn.readInt()
//    var playersList: List[model.Player] = List.empty
//    for (i <- 1 to playersCount){
//      print(s"Enter $i player`s name: ")
//      playersList = model.Player(StdIn.readLine()) :: playersList
//    }

    val players = Vector(Player("Alice"), Player("Renee"))
    val game = GameController(GameState(players = players), new RandomDice)

    game.playTurn()
    game.playTurn()
