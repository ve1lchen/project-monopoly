import scala.io.StdIn
import internal.Dice
import internal.DummyBoard

class GameController(playersCounter: Int):
  var state: GameState = startGame(playersCounter)
  var board: DummyBoard = DummyBoard()

  def init(): Unit =
    var playersList: List[Player] = List()
    for (i <- 1 to playersCounter) {
      println(s"Enter $i player`s name:\n")
      playersList = createPlayer() :: playersList
    }
    state = GameState(players = playersList.reverse.toVector)

  private def createPlayer(): Player =
    val name = StdIn.readLine()
    Player(name=name)

  private def startGame(playersCounter: Int): GameState =
    var playersList: List[Player] = List()
    for (i <- 1 to playersCounter){
      println(s"Enter $i player`s name:\n")
      playersList = createPlayer() :: playersList
    }
    print(playersList.reverse)
    GameState(players = playersList.reverse.toVector)

  def processField(position: Int): Unit =
    val field = board.getField(position)
    if !state.propertyMap.exists(_._2 == position) then
      println(s"Field ${field.name} has no owner.") else
      println(s"Field ${field.name} already has an owner.")

  def playTurn(): Unit =
    val steps: (Int, Int) = Dice.roll()
    state.movePlayer(steps)
    val player: Player = state.players(state.currentPlayerIndex)
    processField(player.position)



@main def main(): Unit =
  val controller = GameController(2)
  controller.playTurn()