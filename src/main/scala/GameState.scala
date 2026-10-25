case class GameState(
    players: Vector[Player],
    currentPlayerIndex: Int = 0,
    isGameOver: Boolean = false,
    propertyMap: Map[Int, Int] // PlayerID:FieldID
):

  def checkGameOver: GameState = // TODO Implement when isBankrupt added to Player class
    var notBankrupts: Int = 0
    for (player <- players) if !player.isBankrupt then notBankrupts += 1
    copy(isGameOver= notBankrupts == 1)

  def nextTurn(isDouble: Boolean): GameState =
    val nextIndex = if isDouble then currentPlayerIndex else (currentPlayerIndex + 1) % players.length
    copy(currentPlayerIndex = nextIndex)

  def movePlayer(steps: Int): GameState =
    val updatedPlayers = players.updated(currentPlayerIndex, players(currentPlayerIndex).move(steps))
    copy(players = updatedPlayers)

  def updatePropertyMap(propertyMap: Map[Int, Int]): GameState = ??? // TODO Implement when the first TODO is done