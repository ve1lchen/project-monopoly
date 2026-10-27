case class GameState(
    players: Vector[Player],
    currentPlayerIndex: Int = 0,
    isGameOver: Boolean = false,
    propertyMap: Map[Int, Int] = Map.empty // PlayerID:FieldID
):

  def checkGameOver: GameState =
    var notBankrupts: Int = 0
    for (player <- players) if !player.isBankrupt then notBankrupts += 1
    copy(isGameOver= notBankrupts == 1)

  def nextTurn(isDouble: Boolean): GameState =
    val nextIndex = if isDouble then currentPlayerIndex else (currentPlayerIndex + 1) % players.length
    copy(currentPlayerIndex = nextIndex)

  def movePlayer(steps: (Int, Int)): GameState =
    val updatedPlayers = players.updated(currentPlayerIndex, players(currentPlayerIndex).move(steps._1 + steps._2))
    copy(players = updatedPlayers)

  def updatePropertyMap(propertyMap: Map[Int, Int], playerId: Int, fieldId:Int): GameState =
    val updatedMap = propertyMap + (playerId -> fieldId)
    copy(propertyMap = updatedMap)
    