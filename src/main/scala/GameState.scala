case class GameState(
    players: Vector[Player],
    currentPlayerIndex: Int = 0,
    gameOver: Boolean = false,
    propertyMap: Map[Int, Player] // TODO Change to [Player, Field] as the Field class is implemented.
):

  def isGameOver: Boolean = ??? // TODO Implement when isBankrupt added to Player class

  def nextTurn(isDouble: Boolean): GameState =
    val nextIndex = if isDouble then currentPlayerIndex else (currentPlayerIndex + 1) % players.length
    copy(currentPlayerIndex = nextIndex)

  def movePlayer(steps: Int): GameState =
    val updatedPlayers = players.updated(currentPlayerIndex, players(currentPlayerIndex).move(steps))
    copy(players = updatedPlayers)

  def updatePropertyMap(propertyMap: Map[Int, Player]): GameState = ??? // TODO Implement when the first TODO is done