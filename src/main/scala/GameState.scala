import internal.DummyBoard

case class GameState(
    players: Vector[Player],
    currentPlayerIndex: Int = 0,
    isGameOver: Boolean = false,
    propertyMap: Map[Int, Int] = Map.empty, // FieldID : PlayerID
    board: DummyBoard = DummyBoard()
):

  def checkGameOver: GameState =
    var notBankrupts: Int = 0
    for (player <- players) if !player.isBankrupt then notBankrupts += 1
    copy(isGameOver= notBankrupts == 1)

  def nextTurn(isDouble: Boolean): GameState =
    val nextIndex = if isDouble then currentPlayerIndex else (currentPlayerIndex + 1) % players.length
    copy(currentPlayerIndex = nextIndex)

  def movePlayer(steps: (Int, Int)): GameState =
    val newPosition = (players(currentPlayerIndex).position + steps._2 + steps._1) % board.size
    val updatedPlayers = players.updated(currentPlayerIndex, players(currentPlayerIndex).changePosition(newPosition))
    copy(players = updatedPlayers)

  def updatePropertyMap(fieldId: Int, playerId:Int): GameState =
    val updatedMap = propertyMap + (fieldId -> playerId)
    copy(propertyMap = updatedMap)

  def getCurrentPlayer: Player =
    players(currentPlayerIndex)