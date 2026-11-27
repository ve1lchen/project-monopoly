package model

case class GameState(
    players: Vector[Player],
    currentPlayerIndex: Int = 0,
    isGameOver: Boolean = false,
    propertyMap: Map[Int, Int] = Map.empty, // FieldId: PlayerID
    levels: Map[Int, DevelopmentLevel] = Map.empty, // FieldId, Level
    board: Board = StandardBoard,
    lastRoll: Option[(Int, (Int, Int))] = None, // PlayerID, rolled
    turnState: TurnState = AwaitingRollState
):

  /**
   * The game is over if there`s only 1 not bankrupted player left
   */
  def checkGameOver: GameState =
    var notBankrupts: Int = 0
    for player <- players do if !player.isBankrupt then notBankrupts += 1
    copy(isGameOver = notBankrupts == 1)

  /**
   * Changes to the next Player and changes turnState = AwaitingRollState
   * @param isDouble both dice have the same number
   */
  def nextTurn(isDouble: Boolean): GameState =
    val nextIndex = if isDouble then currentPlayerIndex
    else (currentPlayerIndex + 1) % players.length
    copy(currentPlayerIndex = nextIndex, turnState = AwaitingRollState)

  def undoNextTurn(isDouble: Boolean): GameState =
    val prevIndex = if isDouble then currentPlayerIndex
    else (currentPlayerIndex - 1 + players.length) % players.length
    copy(currentPlayerIndex = prevIndex, turnState = AwaitingActionState)

  /**
   * Changes player`s position and sets turnState = AwaitingActionState
   * @param rolled the dice roll
   */
  def movePlayer(rolled: (Int, Int)): GameState =
    val currentPlayer = players(currentPlayerIndex)
    val sumRoll = rolled._2 + rolled._1

    val oldPosition = currentPlayer.position
    val newPosition = board.relativePosition(
      oldPosition + sumRoll)

    val events = board.getEvents(oldPosition, sumRoll)

    for i <- 0 until events.size - 1 do events(i) match
      case startField: StartField =>

    val updatedPlayers = players.updated(
      currentPlayerIndex,
      currentPlayer.changePosition(newPosition)
    )
    copy(
      players = updatedPlayers,
      lastRoll = Some(currentPlayerIndex, rolled),
      turnState = AwaitingActionState
    )

  def undoMovePlayer(diceRoll: (Int, Int)): GameState =
    val oldPosition = board.relativePosition(players(currentPlayerIndex)
      .position - diceRoll._1 - diceRoll._2)
    val updatedPlayers = players.updated(
      currentPlayerIndex,
      players(currentPlayerIndex).changePosition(oldPosition)
    )
    copy(
      players = updatedPlayers,
      turnState = AwaitingRollState
    )

  /**
   * Adds new field:player relation to the map
   * @param fieldId the id of the field
   * @param playerId the id of the player
   */
  def updatePropertyMap(fieldId: Int, playerId: Int): GameState =
    val updatedMap = propertyMap + (fieldId -> playerId)
    copy(propertyMap = updatedMap)

  def removePropertyMap(fieldId: Int): GameState =
    val updatedMap = propertyMap - fieldId
    copy(propertyMap = updatedMap)

  def getCurrentPlayer: Player =
    players(currentPlayerIndex)

  def getCurrentField(player: Player = getCurrentPlayer): Field =
    board.get(player.position)

  def getCurrentLevel(player: Player = getCurrentPlayer): Option[DevelopmentLevel] =
    levels.get(player.position)

  def getCurrentFieldOwner: Option[Player] =
    propertyMap.get(getCurrentPlayer.position)
      .flatMap: id =>
        players.find(_.id == id)
