package model

case class GameState(
    players: Vector[Player],
    currentPlayerIndex: Int = 0,
    isGameOver: Boolean = false,
    propertyMap: Map[Int, Int] = Map.empty, // FieldId: PlayerID
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

  /**
   * Changes player`s position and sets turnState = AwaitingActionState
   * @param rolled the dice roll
   */
  def movePlayer(rolled: (Int, Int)): GameState =
    val newPosition = board.relativePosition(players(currentPlayerIndex)
      .position + rolled._2 + rolled._1)
    val updatedPlayers = players.updated(
      currentPlayerIndex,
      players(currentPlayerIndex).changePosition(newPosition)
    )
    copy(
      players = updatedPlayers,
      lastRoll = Some(currentPlayerIndex, (rolled._1, rolled._2)),
      turnState = AwaitingActionState
    )

  /**
   * Adds new field:player relation to the map
   * @param fieldId the id of the field
   * @param playerId the id of the player
   */
  def updatePropertyMap(fieldId: Int, playerId: Int): GameState =
    val updatedMap = propertyMap + (fieldId -> playerId)
    copy(propertyMap = updatedMap)

  def getCurrentPlayer: Player =
    players(currentPlayerIndex)
